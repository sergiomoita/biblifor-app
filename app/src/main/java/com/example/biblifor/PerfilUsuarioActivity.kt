package com.example.biblifor

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class PerfilUsuarioActivity : BaseActivity() {

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil_usuario)

        // 🔹 Campos da tela
        val nomeTextView = findViewById<TextView>(R.id.leoNomeCompletoUserPU5)
        val cursoTextView = findViewById<TextView>(R.id.leoNomeCursoUserPU5)
        val matriculaTextView = findViewById<TextView>(R.id.leoMatriculaUserPU5)

        // 🔹 Exemplo: pegar o ID do documento do aluno (ex: "1")
        // Em um app real, esse valor pode vir da tela de login
        val idAluno = "1"

        // 🔹 Busca os dados do aluno no Firestore
        db.collection("alunos").document(idAluno)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val nome = document.getString("nome") ?: ""
                    val curso = document.getString("curso") ?: ""
                    val matricula = document.getString("matricula") ?: ""

                    nomeTextView.text = nome
                    cursoTextView.text = curso
                    matriculaTextView.text = matricula
                } else {
                    nomeTextView.text = "Usuário não encontrado"
                }
            }
            .addOnFailureListener {
                nomeTextView.text = "Erro ao carregar dados"
            }

        // 🔹 RecyclerView do histórico
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerHistorico5)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.addItemDecoration(
            DividerItemDecoration(this, layoutManager.orientation)
        )

        // 🔹 Lista de exemplo (pode depois puxar do Firebase)
        val historico = listOf(
            HistoricoEmprestimo("Romeu e Julieta - W.Shakespeare 13/09/2025"),
            HistoricoEmprestimo("1984 - George Orwell 02/09/2025"),
            HistoricoEmprestimo("Dom Casmurro - Machado de Assis 13/09/2025")
        )
        recyclerView.adapter = HistoricoEmprestimoAdapter(historico)

        // 🔹 Botões e navegação
        val leoBotaoVoltarPU5 = findViewById<ImageView>(R.id.leoImagemSetaPU5)
        leoBotaoVoltarPU5.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val leoBotaoNotificacoesSPU5 = findViewById<ImageView>(R.id.leoImagemNotificacaoSuperiorPU5)
        leoBotaoNotificacoesSPU5.setOnClickListener {
            val navegarNotificacoesSPU5 = Intent(this, AvisosUsuarioActivity::class.java)
            startActivity(navegarNotificacoesSPU5)
        }

        val leoLogoHomePU5 = findViewById<ImageView>(R.id.leoLogoHome3)
        leoLogoHomePU5.setOnClickListener {
            val navegarLogoHomePU5 = Intent(this, MenuPrincipalUsuarioActivity::class.java)
            startActivity(navegarLogoHomePU5)
        }

        val leoLogoChatBotPU5 = findViewById<ImageView>(R.id.leoImagemChatbot3)
        leoLogoChatBotPU5.setOnClickListener {
            val navegarLogoChatBotPU5 = Intent(this, ChatbotUsuarioActivity::class.java)
            startActivity(navegarLogoChatBotPU5)
        }

        val leoLogoNotificacoesPU5 = findViewById<ImageView>(R.id.leoImagemNotificacoes3)
        leoLogoNotificacoesPU5.setOnClickListener {
            val navegarLogoNotificacoesPU5 = Intent(this, AvisosUsuarioActivity::class.java)
            startActivity(navegarLogoNotificacoesPU5)
        }

        val leoLogoMenuPU5 = findViewById<ImageView>(R.id.leoImagemMenu3)
        leoLogoMenuPU5.setOnClickListener {
            val navegarLogoMenuPU5 = Intent(this, MenuHamburguerUsuarioActivity::class.java)
            startActivity(navegarLogoMenuPU5)
        }
    }
}
