package com.example.biblifor

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ChatbotUsuarioActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_chatbot_usuario)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val leoSetaVoltarChatbot7 = findViewById<ImageView>(R.id.leoImagemSetaVoltarChatbot7)
        leoSetaVoltarChatbot7.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        val leoLogoHomeBFChatbot7 = findViewById<ImageView>(R.id.leoLogoHomeChatbotBF7)
        leoLogoHomeBFChatbot7.setOnClickListener {
            val navegarHomeChat7 = Intent (this, MenuPrincipalUsuarioActivity::class.java)
            startActivity(navegarHomeChat7)
        }
        val leoLogoChatbotBFChatbot7 = findViewById<ImageView>(R.id.leoImagemChatbotBF7)
        leoLogoChatbotBFChatbot7.setOnClickListener {
            val navegarChatbotChat7 = Intent (this, ChatbotUsuarioActivity::class.java)
            startActivity(navegarChatbotChat7)
        }
        val leoLogoNotificacoesBFChatbot7 = findViewById<ImageView>(R.id.leoImagemNotificacoesChatbotBF7)
        leoLogoNotificacoesBFChatbot7.setOnClickListener {
            val navegarNotificacoesChat7 = Intent (this, AvisosUsuarioActivity::class.java)
            startActivity(navegarNotificacoesChat7)
        }
        val leoLogoMenuBFChatbot7 = findViewById<ImageView>(R.id.leoImagemMenuChatbotBF7)
        leoLogoMenuBFChatbot7.setOnClickListener {
            val navegarMenuChat7 = Intent (this, MenuPrincipalUsuarioActivity::class.java)
            startActivity(navegarMenuChat7)
        }
    }
}