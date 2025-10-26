package com.example.biblifor

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.ImageView

class LimiteEmprestimosUsuarioActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_limite_emprestimos_usuario) // nome do XML correspondente

        // 🔙 Botão de voltar (leva para PopupEmprestimoProibidoUsuarioActivity)
        val btnVoltar = findViewById<ImageView>(R.id.btnVoltarLimiteEmprestimosSergio)
        btnVoltar.setOnClickListener {
            val intent = Intent(this, PopupEmprestimoProibidoUsuarioActivity::class.java)
            startActivity(intent)
            finish()
        }

        // 🤖 Chatbot superior (leva para ChatbotUsuarioActivity)
        val iconChatbotSuperior = findViewById<ImageView>(R.id.iconMascoteLimiteEmprestimosSergio)
        iconChatbotSuperior.setOnClickListener {
            val intent = Intent(this, ChatbotUsuarioActivity::class.java)
            startActivity(intent)
        }

        // 🔔 Ícone de notificação (leva para AvisosUsuarioActivity)
        val iconNotificacao = findViewById<ImageView>(R.id.iconNotificacaoLimiteEmprestimosSergio)
        iconNotificacao.setOnClickListener {
            val intent = Intent(this, AvisosUsuarioActivity::class.java)
            startActivity(intent)
        }

        // 🏠 Ícone "Home" inferior (leva para MenuPrincipalUsuarioActivity)
        val iconHome = findViewById<ImageView>(R.id.iconHomeLimiteEmprestimosSergio)
        iconHome.setOnClickListener {
            val intent = Intent(this, MenuPrincipalUsuarioActivity::class.java)
            startActivity(intent)
            finish()
        }

        // 🤖 Chatbot inferior (também leva para ChatbotUsuarioActivity)
        val iconChatbotInferior = findViewById<ImageView>(R.id.iconMascoteInferiorLimiteEmprestimosSergio)
        iconChatbotInferior.setOnClickListener {
            val intent = Intent(this, ChatbotUsuarioActivity::class.java)
            startActivity(intent)
        }

        // 💬 Ícone de mensagem inferior (leva para AvisosUsuarioActivity)
        val iconMensagem = findViewById<ImageView>(R.id.iconMensagemLimiteEmprestimosSergio)
        iconMensagem.setOnClickListener {
            val intent = Intent(this, AvisosUsuarioActivity::class.java)
            startActivity(intent)
        }

        // 🍔 Ícone menu hambúrguer (leva para MenuPrincipalUsuarioActivity)
        val iconMenu = findViewById<ImageView>(R.id.iconMenuInferiorLimiteEmprestimosSergio)
        iconMenu.setOnClickListener {
            val intent = Intent(this, MenuHamburguerUsuario::class.java)
            startActivity(intent)
            finish()
        }

        // 📚 Botão "Histórico de Empréstimos"
        val btnHistorico = findViewById<Button>(R.id.btnHistoricoEmprestimosLimiteEmprestimosSergio)
        btnHistorico.setOnClickListener {
            val intent = Intent(this, HistoricoEmprestimosUsuarioActivity::class.java)
            startActivity(intent)
        }
    }
}