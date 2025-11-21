package com.example.biblifor

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.biblifor.util.base64ToBitmap
import com.example.biblifor.util.bitmapToBase64
import com.google.firebase.firestore.FirebaseFirestore

class PerfilAdministradorActivity : BaseActivity() {

    private lateinit var db: FirebaseFirestore
    private var admIdForEdit: String? = null

    private lateinit var recyclerView: RecyclerView
    private lateinit var historicoAdapter: HistoricoEmprestimoAdapter

    // ==== Selecionar nova foto ====
    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri == null) return@registerForActivityResult

            try {
                contentResolver.openInputStream(uri)?.use { input ->
                    val bitmap = BitmapFactory.decodeStream(input)
                    if (bitmap == null) {
                        Toast.makeText(this, "Não foi possível carregar a imagem.", Toast.LENGTH_SHORT).show()
                        return@registerForActivityResult
                    }

                    val base64 = bitmapToBase64(bitmap)
                    val admId = admIdForEdit
                    if (admId.isNullOrEmpty()) {
                        Toast.makeText(this, "Administrador não identificado.", Toast.LENGTH_SHORT).show()
                        return@registerForActivityResult
                    }

                    db.collection("administrador").document(admId)
                        .update(mapOf("fotoPerfil" to base64))
                        .addOnSuccessListener {
                            findViewById<ImageView>(R.id.leoFotoPerfilAdmPADM42).setImageBitmap(bitmap)
                            Toast.makeText(this, "Foto atualizada!", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Erro ao atualizar foto: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                        }
                }
            } catch (e: Exception) {
                Toast.makeText(this, "Falha ao abrir imagem.", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil_administrador)

        db = FirebaseFirestore.getInstance()

        val nomeHeader = findViewById<TextView>(R.id.leoNomeAdmPADM42)
        val matriculaHeader = findViewById<TextView>(R.id.leoMatriculaAdmPADM42)

        val nomeCompletoBox = findViewById<TextView>(R.id.leoNomeCompletoAdmPADM42)
        val funcaoBox = findViewById<TextView>(R.id.leoNomeFuncaoAdmPADM42)
        val matriculaBox = findViewById<TextView>(R.id.leoMatriculaAdmPADM42)

        val fotoPerfil = findViewById<ImageView>(R.id.leoFotoPerfilAdmPADM42)
        val btnEditarFoto = findViewById<ImageView>(R.id.leoBotaoEditarPerfilAdmNew)

        // =========================
        // 🔹 RecyclerView do histórico REAL
        // =========================
        recyclerView = findViewById(R.id.recyclerHistorico42)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(DividerItemDecoration(this, RecyclerView.VERTICAL))

        historicoAdapter = HistoricoEmprestimoAdapter(mutableListOf())
        recyclerView.adapter = historicoAdapter

        // =========================
        // 🔹 Dados do ADM logado
        // =========================
        val prefs = getSharedPreferences("APP_PREFS", MODE_PRIVATE)
        val matriculaAdm = prefs.getString("MATRICULA_ADM", null)
        admIdForEdit = matriculaAdm

        if (matriculaAdm == null) {
            nomeHeader.text = "Administrador não encontrado"
            nomeCompletoBox.text = "Administrador não encontrado"
        } else {
            db.collection("administrador").document(matriculaAdm)
                .get()
                .addOnSuccessListener { doc ->
                    if (doc.exists()) {
                        val nome = doc.getString("nome") ?: ""
                        val cargo = doc.getString("cargo") ?: ""
                        val matricula = doc.getString("matricula") ?: ""
                        val fotoBase64 = doc.getString("fotoPerfil")

                        nomeHeader.text = nome
                        matriculaHeader.text = matricula

                        nomeCompletoBox.text = nome
                        funcaoBox.text = cargo
                        matriculaBox.text = matricula

                        if (!fotoBase64.isNullOrEmpty()) {
                            base64ToBitmap(fotoBase64)?.let { fotoPerfil.setImageBitmap(it) }
                        }
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("PERFIL_ADM", "Erro: ${e.localizedMessage}")
                }
        }

        // =========================
        // 🔹 Carregar histórico REAL do Firestore
        // =========================
        carregarHistorico()

        // ========= Botões =========
        btnEditarFoto.setOnClickListener { pickImageLauncher.launch("image/*") }

        findViewById<ImageView>(R.id.leoImagemSetaPADM42).setOnClickListener {
            startActivity(Intent(this, MenuPrincipalAdministradorActivity::class.java))
        }

        findViewById<ImageView>(R.id.leoImagemNotificacaoSuperiorPADM42).setOnClickListener {
            startActivity(Intent(this, MensagensAdministradorActivity::class.java))
        }

        findViewById<ImageView>(R.id.iconHomeCapsulasAdmSergio).setOnClickListener {
            startActivity(Intent(this, MenuPrincipalAdministradorActivity::class.java)); finish()
        }

        findViewById<ImageView>(R.id.iconEscreverMsgCapsulasAdmSergio).setOnClickListener {
            startActivity(Intent(this, EscreverMensagemAdministradorActivity::class.java))
        }

        findViewById<ImageView>(R.id.iconMensagemCapsulasAdmSergio).setOnClickListener {
            startActivity(Intent(this, MensagensAdministradorActivity::class.java))
        }

        findViewById<ImageView>(R.id.iconMenuInferiorCapsulasAdmSergio).setOnClickListener {
            startActivity(Intent(this, MenuPrincipalAdministradorActivity::class.java)); finish()
        }
    }

    // =====================================================================
    // 🔥 BUSCA HISTÓRICO REAL DO FIRESTORE
    // =====================================================================
    private fun carregarHistorico() {
        val prefs = getSharedPreferences("APP_PREFS", MODE_PRIVATE)
        val matriculaAdm = prefs.getString("MATRICULA_ADM", null)

        if (matriculaAdm.isNullOrEmpty()) return

        db.collection("administrador")
            .document(matriculaAdm)
            .collection("historicoEmprestimos")
            .get()
            .addOnSuccessListener { lista ->

                val itens = lista.map { doc ->
                    val titulo = doc.getString("nome") ?: "Sem título"
                    val autor = doc.getString("autor") ?: ""
                    val data = doc.getString("dataDevolucao") ?: ""
                    HistoricoEmprestimo("$titulo - $autor   $data")
                }.toMutableList()

                historicoAdapter.updateList(itens)
            }
            .addOnFailureListener {
                Log.e("HISTORICO_ADM", "Erro ao carregar histórico: ${it.localizedMessage}")
            }
    }
}
