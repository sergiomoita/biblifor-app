package com.example.biblifor

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView

class LimiteEmprestimosActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_limite_emprestimos)

        // ==============================
        // BOTÃO VOLTAR
        // ==============================
        val btnVoltar = findViewById<ImageView>(R.id.btnVoltarLimite)
        btnVoltar.setOnClickListener {
            finish()
        }

        // ==============================
        // BOTÃO HISTÓRICO DE EMPRÉSTIMOS
        // ==============================
        val btnHistorico = findViewById<TextView>(R.id.btnHistoricoLimite)
        btnHistorico.setOnClickListener {
            startActivity(Intent(this, HistoricoEmprestimosUsuarioActivity::class.java))
        }

        // ==============================
        // ÍCONE NOTIFICAÇÃO SUPERIOR
        // ==============================
        val btnNotificacao = findViewById<ImageView>(R.id.notificacaoLimite)
        btnNotificacao.setOnClickListener {
            startActivity(Intent(this, AvisosUsuarioActivity::class.java))
        }

        // ==============================
        // BARRA INFERIOR
        // ==============================

        // HOME
        findViewById<ImageView>(R.id.limiteHome).setOnClickListener {
            startActivity(Intent(this, MenuPrincipalUsuarioActivity::class.java))
        }

        // CHATBOT
        findViewById<ImageView>(R.id.limiteChatbot).setOnClickListener {
            startActivity(Intent(this, ChatbotUsuarioActivity::class.java))
        }

        // NOTIFICAÇÕES
        findViewById<ImageView>(R.id.limiteNotificacoes).setOnClickListener {
            startActivity(Intent(this, AvisosUsuarioActivity::class.java))
        }

        // MENU HAMBÚRGUER
        findViewById<ImageView>(R.id.limiteMenu).setOnClickListener {
            startActivity(Intent(this, MenuHamburguerUsuarioActivity::class.java))
        }
    }
}
