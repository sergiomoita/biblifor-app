package com.example.biblifor

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.biblifor.R

class MensagemSemResultadoUsuarioActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mensagem_sem_resultado_usuario)

        // Botão de voltar
        val btnVoltar = findViewById<ImageView>(R.id.btnVoltarMensagemSemResultadoUsuarioSergio)
        btnVoltar.setOnClickListener {
            val intent = Intent(this, MenuPrincipalUsuarioActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Campo de pesquisa e ícone da lupa
        val editPesquisa = findViewById<EditText>(R.id.editPesquisaMensagemSemResultadoUsuarioSergio)
        val iconLupa = findViewById<ImageView>(R.id.iconLupaMensagemSemResultadoUsuarioSergio)

        iconLupa.setOnClickListener {
            val termo = editPesquisa.text.toString().trim()
            if (termo.isNotEmpty()) {
                Toast.makeText(this, "Pesquisando por \"$termo\"...", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Digite um termo para pesquisar.", Toast.LENGTH_SHORT).show()
            }
        }

        // 🤖 Ícone do mascote superior → ChatbotUsuarioActivity
        val iconMascoteSuperior = findViewById<ImageView>(R.id.iconMascoteMensagemSemResultadoUsuarioSergio)
        iconMascoteSuperior.setOnClickListener {
            val intent = Intent(this, ChatbotUsuarioActivity::class.java)
            startActivity(intent)
        }

        // 🔔 Ícone de notificação → AvisosUsuarioActivity
        val iconNotificacao = findViewById<ImageView>(R.id.iconNotificacaoMensagemSemResultadoUsuarioSergio)
        iconNotificacao.setOnClickListener {
            val intent = Intent(this, AvisosUsuarioActivity::class.java)
            startActivity(intent)
        }

        // ==============================
        // ⚙️ Funções da Barra Inferior
        // ==============================

        // 🏠 Home → MenuPrincipalUsuarioActivity
        val iconHome = findViewById<ImageView>(R.id.iconHomeMensagemSemResultadoUsuarioSergio)
        iconHome.setOnClickListener {
            val intent = Intent(this, MenuPrincipalUsuarioActivity::class.java)
            startActivity(intent)
            finish()
        }

        // 🤖 Chatbot inferior → ChatbotUsuarioActivity
        val iconMascoteInferior = findViewById<ImageView>(R.id.iconChatBotMensagemSemResultadoUsuarioSergio)
        iconMascoteInferior.setOnClickListener {
            val intent = Intent(this, ChatbotUsuarioActivity::class.java)
            startActivity(intent)
        }

        // 💬 Mensagem inferior → AvisosUsuarioActivity
        val iconMensagem = findViewById<ImageView>(R.id.iconMensagemMensagemSemResultadoUsuarioSergio)
        iconMensagem.setOnClickListener {
            val intent = Intent(this, AvisosUsuarioActivity::class.java)
            startActivity(intent)
        }

        // 🍔 Menu inferior → MenuPrincipalUsuarioActivity
        val iconMenu = findViewById<ImageView>(R.id.iconMenuMensagemSemResultadoUsuarioSergio)
        iconMenu.setOnClickListener {
            val intent = Intent(this, MenuHamburguerUsuarioActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}