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

class PerfilUsuarioActivity : BaseActivity() {

    private val db = FirebaseFirestore.getInstance()

    private var alunoIdForEdit: String? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var historicoAdapter: HistoricoEmprestimoAdapter

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
                    val idAluno = alunoIdForEdit
                    if (idAluno.isNullOrEmpty()) {
                        Toast.makeText(this, "Usuário não identificado.", Toast.LENGTH_SHORT).show()
                        return@registerForActivityResult
                    }

                    db.collection("alunos").document(idAluno)
                        .update(mapOf("fotoPerfil" to base64))
                        .addOnSuccessListener {
                            findViewById<ImageView>(R.id.leoFotoPerfilUsuarioPU5).setImageBitmap(bitmap)
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
        setContentView(R.layout.activity_perfil_usuario)

        val nomeBoxTextView = findViewById<TextView>(R.id.leoNomeCompletoUserPU5)
        val cursoTextView   = findViewById<TextView>(R.id.leoNomeCursoUserPU5)
        val matriculaBoxTv  = findViewById<TextView>(R.id.leoMatriculaUserPU5)

        val nomeHeaderTextView = findViewById<TextView>(R.id.leoNomeUserPU5)
        val matriculaHeaderTv  = findViewById<TextView>(R.id.leoMatriculaUserPU5)
        val fotoPerfilImage    = findViewById<ImageView>(R.id.leoFotoPerfilUsuarioPU5)
        val editarFotoPerfilNew = findViewById<ImageView>(R.id.leoBotaoEditarFotoPerfilNew)

        val prefs = getSharedPreferences("APP_PREFS", MODE_PRIVATE)
        val idAluno = prefs.getString("MATRICULA_USER", null)
        alunoIdForEdit = idAluno

        if (idAluno == null) {
            nomeHeaderTextView.text = "Usuário não encontrado"
            nomeBoxTextView.text = "Usuário não encontrado"
            Log.e("PERFIL", "Nenhuma matrícula salva em APP_PREFS")
        } else {
            db.collection("alunos").document(idAluno)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val nome = document.getString("nome") ?: ""
                        val curso = document.getString("curso") ?: ""
                        val matricula = document.getString("matricula") ?: ""
                        val fotoBase64 = document.getString("fotoPerfil")

                        nomeHeaderTextView.text = nome
                        matriculaHeaderTv.text = matricula

                        nomeBoxTextView.text = nome
                        cursoTextView.text = curso
                        matriculaBoxTv.text = matricula

                        if (!fotoBase64.isNullOrEmpty()) {
                            base64ToBitmap(fotoBase64)?.let { fotoPerfilImage.setImageBitmap(it) }
                        }
                    } else {
                        nomeHeaderTextView.text = "Usuário não encontrado"
                        nomeBoxTextView.text = "Usuário não encontrado"
                    }
                }
                .addOnFailureListener { e ->
                    nomeHeaderTextView.text = "Erro ao carregar dados"
                    nomeBoxTextView.text = "Erro ao carregar dados"
                    Log.e("PERFIL", "Erro ao buscar aluno: ${e.localizedMessage}")
                }
        }

        editarFotoPerfilNew.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        // ===============================
        // 🔹 CONFIGURAÇÃO DO RECYCLER VIEW
        // ===============================
        recyclerView = findViewById(R.id.recyclerHistorico5)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(
            DividerItemDecoration(this, RecyclerView.VERTICAL)
        )

        // 🔥 CORREÇÃO: instanciar o adapter SEM lista!
        historicoAdapter = HistoricoEmprestimoAdapter()
        recyclerView.adapter = historicoAdapter

        carregarHistorico(idAluno)

        // Navegação
        findViewById<ImageView>(R.id.leoImagemSetaPU5).setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        findViewById<ImageView>(R.id.leoImagemNotificacaoSuperiorPU5).setOnClickListener {
            startActivity(Intent(this, AvisosUsuarioActivity::class.java))
        }

        findViewById<ImageView>(R.id.leoLogoHome3).setOnClickListener {
            startActivity(Intent(this, MenuPrincipalUsuarioActivity::class.java))
        }

        findViewById<ImageView>(R.id.leoImagemChatbot3).setOnClickListener {
            startActivity(Intent(this, ChatbotUsuarioActivity::class.java))
        }

        findViewById<ImageView>(R.id.leoImagemNotificacoes3).setOnClickListener {
            startActivity(Intent(this, AvisosUsuarioActivity::class.java))
        }

        findViewById<ImageView>(R.id.leoImagemMenu3).setOnClickListener {
            startActivity(Intent(this, MenuHamburguerUsuarioActivity::class.java))
        }
    }

    // ==================================================
    // 🔥 CARREGA O HISTÓRICO REAL DO FIRESTORE
    // ==================================================
    private fun carregarHistorico(idAluno: String?) {

        if (idAluno.isNullOrEmpty()) return

        db.collection("alunos")
            .document(idAluno)
            .collection("historicoEmprestimos")
            .get()
            .addOnSuccessListener { lista ->

                val itens = lista.map { doc ->
                    val titulo = doc.getString("nome") ?: "Sem título"
                    val autor = doc.getString("autor") ?: ""
                    val data = doc.getString("dataDevolucao") ?: ""
                    HistoricoEmprestimo("$titulo - $autor  $data")
                }.toMutableList()

                // 🔥 AGORA FUNCIONA
                historicoAdapter.updateList(itens)
            }
            .addOnFailureListener {
                Log.e("HISTORICO", "Erro ao carregar histórico: ${it.localizedMessage}")
            }
    }
}
