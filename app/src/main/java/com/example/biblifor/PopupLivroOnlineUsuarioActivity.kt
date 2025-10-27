package com.example.biblifor

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.biblifor.R

class PopupLivroOnlineUsuarioActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_popup_livro_online_usuario)

        // 🔙 Botão Voltar
        val btnVoltar = findViewById<ImageView>(R.id.btnVoltarPopupLivroOnlineUsuarioSergio)
        btnVoltar.setOnClickListener {
            finish() // volta para a tela anterior
        }

        // 🌐 Botão Online
        val btnOnline = findViewById<Button>(R.id.btnOnlinePopupLivroOnlineUsuarioSergio)
        btnOnline.setOnClickListener {
            mostrarToastCustomizado()
        }

        // ==============================
        // ⚙️ Funções da Barra Inferior
        // ==============================

        // 🏠 Home → MenuPrincipalUsuarioActivity
        val iconHome = findViewById<ImageView>(R.id.iconHomePopupLivroOnlineUsuarioSergio)
        iconHome.setOnClickListener {
            val intent = Intent(this, MenuPrincipalUsuarioActivity::class.java)
            startActivity(intent)
            finish()
        }

        // 🤖 Chatbot inferior → ChatbotUsuarioActivity
        val iconMascoteInferior = findViewById<ImageView>(R.id.iconChatBotPopupLivroOnlineUsuarioSergio)
        iconMascoteInferior.setOnClickListener {
            val intent = Intent(this, ChatbotUsuarioActivity::class.java)
            startActivity(intent)
        }

        // 💬 Mensagem inferior → AvisosUsuarioActivity
        val iconMensagem = findViewById<ImageView>(R.id.iconMensagemPopupLivroOnlineUsuarioSergio)
        iconMensagem.setOnClickListener {
            val intent = Intent(this, AvisosUsuarioActivity::class.java)
            startActivity(intent)
        }

        // 🍔 Menu inferior → MenuPrincipalUsuarioActivity
        val iconMenu = findViewById<ImageView>(R.id.iconMenuPopupLivroOnlineUsuarioSergio)
        iconMenu.setOnClickListener {
            val intent = Intent(this, MenuPrincipalUsuarioActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    // 💬 Toast personalizado simulando pop-up azul da imagem
    private fun mostrarToastCustomizado() {
        val dialog = Dialog(this)
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_toast_online, null)
        dialog.setContentView(view)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setCancelable(true)

        val btnSim = view.findViewById<Button>(R.id.btnSimToast)
        val btnNao = view.findViewById<Button>(R.id.btnNaoToast)

        btnSim.setOnClickListener {
            dialog.dismiss()
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://minhabiblioteca.unifor.com"))
            startActivity(intent)
        }

        btnNao.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}
