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


        val imgLivro = findViewById<ImageView>(R.id.lopesLivroRomeuJulieta32)
        imgLivro.setOnClickListener {
            val navegarLivro = Intent(this, RenovacaoEmprestimoUsuarioActivity::class.java)
            startActivity(navegarLivro)
        }

        val imgSeta = findViewById<ImageView>(R.id.lopesSetaVoltar32)
        imgSeta.setOnClickListener {
            finish()
        }


        val imagemLogoHome3 = findViewById<ImageView>(R.id.leoLogoHome3)
        imagemLogoHome3.setOnClickListener {
            val navegarLogoHome3 = Intent(this, MenuPrincipalUsuarioActivity::class.java)
            startActivity(navegarLogoHome3)
        }

        val imagemLogoChatBot3 = findViewById<ImageView>(R.id.leoImagemChatbot3)
        imagemLogoChatBot3.setOnClickListener {
            val navegarLogoChatBot3 = Intent(this, ChatbotUsuarioActivity::class.java)
            startActivity(navegarLogoChatBot3)
        }

        val imagemLogoNotificacoes3 = findViewById<ImageView>(R.id.leoImagemNotificacoes3)
        imagemLogoNotificacoes3.setOnClickListener {
            val navegarLogoNotificacoes3 = Intent(this, AvisosUsuarioActivity::class.java)
            startActivity(navegarLogoNotificacoes3)
        }

        val imagemLogoMenu3 = findViewById<ImageView>(R.id.leoImagemMenu3)
        imagemLogoMenu3.setOnClickListener {
            val navegarLogoMenu3 = Intent(this, MenuHamburguerUsuarioActivity::class.java)
            startActivity(navegarLogoMenu3)
        }



    }
}