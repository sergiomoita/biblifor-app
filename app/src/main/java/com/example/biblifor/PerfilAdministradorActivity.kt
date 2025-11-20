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

    // guarda a matrícula do ADM para usar no callback do seletor
    private var admIdForEdit: String? = null

    // Abre seletor externo (Google Fotos/galeria) e trata o retorno
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
                            // reflete na UI imediatamente
                            findViewById<ImageView>(R.id.leoFotoPerfilAdmPADM42).setImageBitmap(bitmap)
                            Toast.makeText(this, "Foto de perfil atualizada!", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Erro ao atualizar foto: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                        }
                }
            } catch (e: Exception) {
                Toast.makeText(this, "Falha ao abrir a imagem.", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil_administrador)

        db = FirebaseFirestore.getInstance()

        // ===== RecyclerView de histórico (exemplo fixo) =====
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerHistorico42)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.addItemDecoration(DividerItemDecoration(this, layoutManager.orientation))

        val historico = listOf(
            HistoricoEmprestimo("Mensagem de Teste 18/09/2025"),
            HistoricoEmprestimo("Tarde de Leitura 02/09/2025"),
            HistoricoEmprestimo("Oficina Escrita 28/08/2025"),
            HistoricoEmprestimo("Ajudar Alunos 19/08/2025"),
            HistoricoEmprestimo("Monitores Biblioteca 04/08/2025"),
            HistoricoEmprestimo("Como Consultar 04/08/2025")
        )
        recyclerView.adapter = HistoricoEmprestimoAdapter(historico)

        // ===== Views de texto e foto =====
        val nomeHeaderTextView = findViewById<TextView>(R.id.leoNomeAdmPADM42)
        val matriculaHeaderTextView = findViewById<TextView>(R.id.leoMatriculaAdmPADM42)

        val nomeCompletoBoxTextView = findViewById<TextView>(R.id.leoNomeCompletoAdmPADM42)
        val funcaoTextView = findViewById<TextView>(R.id.leoNomeFuncaoAdmPADM42)
        val matriculaBoxTextView = findViewById<TextView>(R.id.leoMatriculaAdmPADM42)

        val fotoPerfilImageView = findViewById<ImageView>(R.id.leoFotoPerfilAdmPADM42)
        val botaoEditarPerfilAdmNew = findViewById<ImageView>(R.id.leoBotaoEditarPerfilAdmNew)

        // ===== Recupera ADM logado do SharedPreferences =====
        val prefs = getSharedPreferences("APP_PREFS", MODE_PRIVATE)
        val matriculaAdm = prefs.getString("MATRICULA_ADM", null)
        val nomeAdmPrefs = prefs.getString("NOME_ADM", null)
        admIdForEdit = matriculaAdm

        if (matriculaAdm == null) {
            nomeHeaderTextView.text = "Administrador não encontrado"
            nomeCompletoBoxTextView.text = "Administrador não encontrado"
            Log.e("PERFIL_ADM", "Nenhuma MATRÍCULA_ADM salva em APP_PREFS")
        } else {
            // Busca documento do administrador logado
            db.collection("administrador")
                .document(matriculaAdm)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val nome = document.getString("nome") ?: ""
                        val cargo = document.getString("cargo") ?: ""
                        val matricula = document.getString("matricula") ?: ""
                        val fotoBase64 = document.getString("fotoPerfil")

                        // Cabeçalho
                        nomeHeaderTextView.text =
                            if (nome.isNotEmpty()) nome else (nomeAdmPrefs ?: "Administrador")
                        matriculaHeaderTextView.text =
                            if (matricula.isNotEmpty()) matricula else matriculaAdm

                        // Boxes de informação
                        nomeCompletoBoxTextView.text = if (nome.isNotEmpty()) nome else "Nome não encontrado"
                        funcaoTextView.text = if (cargo.isNotEmpty()) cargo else "Cargo não encontrado"
                        matriculaBoxTextView.text =
                            if (matricula.isNotEmpty()) matricula else "Sem matrícula"

                        // Foto de perfil (Base64 -> Bitmap)
                        if (!fotoBase64.isNullOrEmpty()) {
                            val bitmap = base64ToBitmap(fotoBase64)
                            if (bitmap != null) {
                                fotoPerfilImageView.setImageBitmap(bitmap)
                            } else {
                                Log.e("PERFIL_ADM", "Falha ao decodificar fotoPerfil para $matriculaAdm")
                            }
                        } else {
                            Log.d("PERFIL_ADM", "fotoPerfil vazio para $matriculaAdm")
                        }
                    } else {
                        nomeHeaderTextView.text = "Administrador não encontrado"
                        nomeCompletoBoxTextView.text = "Administrador não encontrado"
                    }
                }
                .addOnFailureListener { e ->
                    nomeHeaderTextView.text = "Erro ao carregar dados"
                    nomeCompletoBoxTextView.text = "Erro ao carregar dados"
                    Log.e("PERFIL_ADM", "Erro ao buscar administrador: ${e.localizedMessage}")
                }
        }

        // ✏️ Editar foto do perfil do ADM (abre seletor)
        botaoEditarPerfilAdmNew.setOnClickListener {
            if (admIdForEdit.isNullOrEmpty()) {
                Toast.makeText(this, "Administrador não identificado.", Toast.LENGTH_SHORT).show()
            } else {
                pickImageLauncher.launch("image/*")
            }
        }

        // 🔙 Botão de voltar
        findViewById<ImageView>(R.id.leoImagemSetaPADM42).setOnClickListener {
            startActivity(Intent(this, MenuPrincipalAdministradorActivity::class.java))
        }

        // 🔔 Notificações
        findViewById<ImageView>(R.id.leoImagemNotificacaoSuperiorPADM42).setOnClickListener {
            startActivity(Intent(this, MensagensAdministradorActivity::class.java))
        }

        // ⚙️ Barra inferior
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
}
