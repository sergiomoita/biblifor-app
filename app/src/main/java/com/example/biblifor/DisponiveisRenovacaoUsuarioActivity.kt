package com.example.biblifor

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class DisponiveisRenovacaoUsuarioActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_disponiveis_renovacao_usuario)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val imgChatBot = findViewById<ImageView>(R.id.lopesChatBot32)
        imgChatBot.setOnClickListener {
            val navegarChatBot = Intent(this, ChatbotUsuarioActivity::class.java)
            startActivity(navegarChatBot)
        }

        val imgNotificacao = findViewById<ImageView>(R.id.lopesNotificacao32)
        imgNotificacao.setOnClickListener {
            val navegarNotificacao = Intent(this, AvisosUsuarioActivity::class.java)
            startActivity(navegarNotificacao)
        }

        val imgLivro = findViewById<ImageView>(R.id.lopesLivroRomeuJulieta32)
        imgLivro.setOnClickListener {
            val navegarLivro = Intent(this, RenovacaoEmprestimoUsuarioActivity::class.java)
            startActivity(navegarLivro)
        }

        val imgSeta = findViewById<ImageView>(R.id.lopesSetaVoltar32)
        imgSeta.setOnClickListener {
            val navegarSeta = Intent(this, MenuPrincipalUsuarioActivity::class.java)
            startActivity(navegarSeta)
        }




    }
}