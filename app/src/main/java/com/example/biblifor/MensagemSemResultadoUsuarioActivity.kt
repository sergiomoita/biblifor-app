package com.example.biblifor

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView

class MensagemSemResultadoUsuarioActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mensagem_sem_resultado_usuario)

        // --- RECEBER O TERMO DA PESQUISA ---
        val termoPesquisado = intent.getStringExtra("pesquisa") ?: ""

        val tvTermo = findViewById<TextView>(R.id.tvTermoPesquisadoMensagemSemResultadoUsuarioSergio)
        tvTermo.text = "\"$termoPesquisado\""

        // Botão voltar
        val btnVoltar = findViewById<ImageView>(R.id.btnVoltarMensagemSemResultadoUsuarioSergio)
        btnVoltar.setOnClickListener {
            val intent = Intent(this, MenuPrincipalUsuarioActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Campo de pesquisa e ícone da lupa
        val editPesquisa = findViewById<EditText>(R.id.editPesquisaMensagemSemResultadoUsuarioSergio)
        val iconLupa = findViewById<ImageView>(R.id.iconLupaMensagemSemResultadoUsuarioSergio)

        // 🔥 PESQUISA INTELIGENTE REAL — mesma lógica das outras telas
        iconLupa.setOnClickListener {
            val termo = editPesquisa.text.toString().trim()
            if (termo.isNotEmpty()) {
                val intent = Intent(this, ResultadosPesquisaUsuarioActivity::class.java)
                intent.putExtra("pesquisa", termo)
                startActivity(intent)
                finish()
            }
        }

        // Ícone mascote superior → ChatbotUsuarioActivity
        findViewById<ImageView>(R.id.iconMascoteMensagemSemResultadoUsuarioSergio).setOnClickListener {
            startActivity(Intent(this, ChatbotUsuarioActivity::class.java))
        }

        // Notificações superior
        findViewById<ImageView>(R.id.iconNotificacaoMensagemSemResultadoUsuarioSergio).setOnClickListener {
            startActivity(Intent(this, AvisosUsuarioActivity::class.java))
        }

        // ==============================
        // Barra inferior
        // ==============================

        findViewById<ImageView>(R.id.iconHomeMensagemSemResultadoUsuarioSergio).setOnClickListener {
            startActivity(Intent(this, MenuPrincipalUsuarioActivity::class.java))
            finish()
        }

        findViewById<ImageView>(R.id.iconChatBotMensagemSemResultadoUsuarioSergio).setOnClickListener {
            startActivity(Intent(this, ChatbotUsuarioActivity::class.java))
        }

        findViewById<ImageView>(R.id.iconMensagemMensagemSemResultadoUsuarioSergio).setOnClickListener {
            startActivity(Intent(this, AvisosUsuarioActivity::class.java))
        }

        findViewById<ImageView>(R.id.iconMenuMensagemSemResultadoUsuarioSergio).setOnClickListener {
            startActivity(Intent(this, MenuPrincipalUsuarioActivity::class.java))
            finish()
        }
    }
}
