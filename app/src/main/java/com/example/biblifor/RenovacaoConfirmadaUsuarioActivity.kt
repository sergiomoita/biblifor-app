package com.example.biblifor

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.widget.Button
import android.widget.ImageView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class RenovacaoConfirmadaUsuarioActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_renovacao_confirmada_usuario)

        // Ajuste de bordas (Edge-to-Edge)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // ============================
        // 🔥 RECEBENDO A IMAGEM DO LIVRO
        // ============================
        val imagemBase64 = intent.getStringExtra("imagemBase64")
        val imgLivro = findViewById<ImageView>(R.id.imagemLivroConfirmado)

        if (!imagemBase64.isNullOrEmpty()) {
            try {
                val bytes = Base64.decode(imagemBase64, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                imgLivro.setImageBitmap(bitmap)
            } catch (e: Exception) {
                imgLivro.setImageResource(R.drawable.livro_romeu2) // fallback
            }
        } else {
            imgLivro.setImageResource(R.drawable.livro_romeu2)
        }

        // ============================
        // 🔙 Barra inferior - navegação
        // ============================
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

        // ============================
        // 📚 Botão → Histórico
        // ============================
        findViewById<Button>(R.id.lopesBtnHistorico34).setOnClickListener {
            startActivity(Intent(this, HistoricoEmprestimosUsuarioActivity::class.java))
        }
    }
}
