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
import com.example.biblifor.model.Aviso
import com.example.biblifor.util.base64ToBitmap
import com.example.biblifor.util.bitmapToBase64
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class PerfilAdministradorActivity : BaseActivity() {

    private lateinit var db: FirebaseFirestore
    private var admIdForEdit: String? = null

    private lateinit var rvEventos: RecyclerView
    private lateinit var adapterEventos: HistoricoEmprestimoAdapter   // usa o mesmo adapter

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

        // =========================
        // 🔹 Cabeçalho
        // =========================
        val nomeHeader = findViewById<TextView>(R.id.leoNomeAdmPADM42)
        val matriculaHeader = findViewById<TextView>(R.id.leoMatriculaAdmPADM42)

        val nomeCompletoBox = findViewById<TextView>(R.id.leoNomeCompletoAdmPADM42)
        val funcaoBox = findViewById<TextView>(R.id.leoNomeFuncaoAdmPADM42)
        val matriculaBox = findViewById<TextView>(R.id.leoMatriculaAdmPADM42)

        val fotoPerfil = findViewById<ImageView>(R.id.leoFotoPerfilAdmPADM42)
        val btnEditarFoto = findViewById<ImageView>(R.id.leoBotaoEditarPerfilAdmNew)

        // =========================
        // 🔹 RecyclerView — EXIBE EVENTOS DO ADM
        // =========================
        rvEventos = findViewById(R.id.rvEventosPerfilAdm)
        rvEventos.layoutManager = LinearLayoutManager(this)
        rvEventos.addItemDecoration(DividerItemDecoration(this, RecyclerView.VERTICAL))

        adapterEventos = HistoricoEmprestimoAdapter(mutableListOf())
        rvEventos.adapter = adapterEventos

        // =========================
        // 🔹 Dados do administrador logado
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
        }

        // =========================
        // 🔹 CARREGAR EVENTOS DO ADM
        // =========================
        carregarEventosDoAdministrador()

        // =========================
        // 🔹 Botões
        // =========================
        btnEditarFoto.setOnClickListener { pickImageLauncher.launch("image/*") }

        findViewById<ImageView>(R.id.leoImagemSetaPADM42).setOnClickListener {
            startActivity(Intent(this, MenuPrincipalAdministradorActivity::class.java))
        }

        findViewById<ImageView>(R.id.leoImagemNotificacaoSuperiorPADM42).setOnClickListener {
            startActivity(Intent(this, MensagensAdministradorActivity::class.java))
        }

        findViewById<ImageView>(R.id.iconHomeCapsulasAdmSergio).setOnClickListener {
            startActivity(Intent(this, MenuPrincipalAdministradorActivity::class.java))
            finish()
        }

        findViewById<ImageView>(R.id.iconEscreverMsgCapsulasAdmSergio).setOnClickListener {
            startActivity(Intent(this, EscreverMensagemAdministradorActivity::class.java))
        }

        findViewById<ImageView>(R.id.iconMensagemCapsulasAdmSergio).setOnClickListener {
            startActivity(Intent(this, MensagensAdministradorActivity::class.java))
        }

        findViewById<ImageView>(R.id.iconMenuInferiorCapsulasAdmSergio).setOnClickListener {
            startActivity(Intent(this, MenuPrincipalAdministradorActivity::class.java))
            finish()
        }
    }

    // =====================================================================
    // 🔥 CARREGA EVENTOS DO ADMINISTRADOR (MESMO DO MENU PRINCIPAL)
    // =====================================================================
    private fun carregarEventosDoAdministrador() {
        val prefs = getSharedPreferences("APP_PREFS", MODE_PRIVATE)
        val matriculaAdm = prefs.getString("MATRICULA_ADM", null)

        if (matriculaAdm.isNullOrEmpty()) return

        db.collection("mensagens")
            .whereEqualTo("matriculaAdm", matriculaAdm)
            .orderBy("data", Query.Direction.DESCENDING)
            .limit(3)
            .get()
            .addOnSuccessListener { result ->

                val itensRV = result.map { doc ->
                    val titulo = doc.getString("titulo") ?: "Sem título"
                    val mensagem = doc.getString("mensagem") ?: ""
                    val data = doc.getTimestamp("data")?.toDate().toString()

                    HistoricoEmprestimo("$titulo • $mensagem\n$data")
                }

                adapterEventos.updateList(itensRV.toMutableList())
            }
            .addOnFailureListener { e ->
                Log.e("EVENTOS_ADM", "Erro ao carregar eventos: ${e.localizedMessage}")
            }
    }
}
