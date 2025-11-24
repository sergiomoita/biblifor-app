package com.example.biblifor

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PerguntasFrequentesUsuarioActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_perguntas_frequentes_usuario)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val imagemLogoHomePF31 = findViewById<ImageView>(R.id.leoLogoHomeBF31)
        imagemLogoHomePF31.setOnClickListener {
            val navegarLogoHomePF31 = Intent(this, MenuPrincipalUsuarioActivity::class.java)
            startActivity(navegarLogoHomePF31)
        }

        val imagemLogoChatBotPF31 = findViewById<ImageView>(R.id.leoImagemChatbotBF31)
        imagemLogoChatBotPF31.setOnClickListener {
            val navegarLogoChatBotPF31 = Intent(this, ChatbotUsuarioActivity::class.java)
            startActivity(navegarLogoChatBotPF31)
        }

        val imagemLogoNotificacoesPF31 = findViewById<ImageView>(R.id.leoImagemNotificacoesBF31)
        imagemLogoNotificacoesPF31.setOnClickListener {
            val navegarLogoNotificacoesPF31 = Intent(this, AvisosUsuarioActivity::class.java)
            startActivity(navegarLogoNotificacoesPF31)
        }

        val imagemLogoMenuPF31 = findViewById<ImageView>(R.id.leoImagemMenuBF31)
        imagemLogoMenuPF31.setOnClickListener {
            val navegarLogoMenuPF31 = Intent(this, MenuHamburguerUsuarioActivity::class.java)
            startActivity(navegarLogoMenuPF31)
        }
        val imagemSetaVoltarPF31 = findViewById<ImageView>(R.id.leoImagemSetaPF31)
        imagemSetaVoltarPF31.setOnClickListener {
            val navegarPrincipal = Intent(this, MenuPrincipalUsuarioActivity::class.java)
            startActivity(navegarPrincipal)
        }
        val imagemSuperiorChatBotPF31 = findViewById<ImageView>(R.id.leoImagemChatbotSuperiorPF31)
        imagemSuperiorChatBotPF31.setOnClickListener {
            val navegarSuperiorChatBotPF31 = Intent(this, ChatbotUsuarioActivity::class.java)
            startActivity(navegarSuperiorChatBotPF31)
        }
        val imagemNotificacoesSuperiorPF31 = findViewById<ImageView>(R.id.leoImagemNotificacaoSuperiorPF31)
        imagemNotificacoesSuperiorPF31.setOnClickListener {
            val navegarNotificacoesSuperiorPF31 = Intent(this, AvisosUsuarioActivity::class.java)
            startActivity(navegarNotificacoesSuperiorPF31)
        }
    }
}