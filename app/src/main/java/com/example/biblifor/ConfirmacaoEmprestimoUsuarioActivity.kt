package com.example.biblifor

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ConfirmacaoEmprestimoUsuarioActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_confirmacao_emprestimo_usuario)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val botaoHistorico = findViewById<Button>(R.id.lopesBtnHistorico24)
        botaoHistorico.setOnClickListener {
            val navegarHistorico = Intent(this, HistoricoEmprestimosUsuarioActivity::class.java)
            startActivity(navegarHistorico)
        }

        val imgChatBot = findViewById<ImageView>(R.id.lopesChatBot24)
        imgChatBot.setOnClickListener {
            val navegarChatBot = Intent(this, ChatbotUsuarioActivity::class.java)
            startActivity(navegarChatBot)
        }

        val imgNotificacao = findViewById<ImageView>(R.id.lopesNotificacao24)
        imgNotificacao.setOnClickListener {
            val navegarNotificacao = Intent(this, AvisosUsuarioActivity::class.java)
            startActivity(navegarNotificacao)
        }

    }
}
