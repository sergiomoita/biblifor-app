package com.example.biblifor

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class CapsulasUsuarioActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_capsulas_usuario)

        // 🔙 Botão de voltar → MenuPrincipalUsuarioActivity
        val btnVoltar = findViewById<ImageView>(R.id.btnVoltarCapsulasCapsulasUsuarioSergio)
        btnVoltar.setOnClickListener {
            val intent = Intent(this, MenuPrincipalUsuarioActivity::class.java)
            startActivity(intent)
            finish()
        }

        // 🤖 Ícone do mascote superior → ChatbotUsuarioActivity
        val iconMascoteSuperior = findViewById<ImageView>(R.id.iconMascoteCapsulasCapsulasUsuarioSergio)
        iconMascoteSuperior.setOnClickListener {
            val intent = Intent(this, ChatbotUsuarioActivity::class.java)
            startActivity(intent)
        }

        // 🔔 Ícone de notificação → AvisosUsuarioActivity
        val iconNotificacao = findViewById<ImageView>(R.id.iconNotificacaoCapsulasUsuarioSergio)
        iconNotificacao.setOnClickListener {
            val intent = Intent(this, AvisosUsuarioActivity::class.java)
            startActivity(intent)
        }

        // ==============================
        // ⚙️ Funções da Barra Inferior
        // ==============================
        val iconHome = findViewById<ImageView>(R.id.iconHomeCapsulasUsuarioSergio)
        iconHome.setOnClickListener {
            val intent = Intent(this, MenuPrincipalUsuarioActivity::class.java)
            startActivity(intent)
            finish()
        }

        val iconMascoteInferior = findViewById<ImageView>(R.id.iconMascoteInferiorCapsulasUsuarioSergio)
        iconMascoteInferior.setOnClickListener {
            val intent = Intent(this, ChatbotUsuarioActivity::class.java)
            startActivity(intent)
        }

        val iconMensagem = findViewById<ImageView>(R.id.iconMensagemCapsulasUsuarioSergio)
        iconMensagem.setOnClickListener {
            val intent = Intent(this, AvisosUsuarioActivity::class.java)
            startActivity(intent)
        }

        val iconMenu = findViewById<ImageView>(R.id.iconMenuInferiorCapsulasUsuarioSergio)
        iconMenu.setOnClickListener {
            val intent = Intent(this, MenuPrincipalUsuarioActivity::class.java)
            startActivity(intent)
            finish()
        }

        // ==============================
        // 🎯 Clique nas cápsulas
        // ==============================
        val idsCapsulas = listOf(
            R.id.btnCapsula1CapsulasUsuarioSergio,
            R.id.btnCapsula2CapsulasUsuarioSergio,
            R.id.btnCapsula3CapsulasUsuarioSergio,
            R.id.btnCapsula4CapsulasUsuarioSergio,
            R.id.btnCapsula5CapsulasUsuarioSergio,
            R.id.btnCapsula6CapsulasUsuarioSergio,
            R.id.btnCapsula7CapsulasUsuarioSergio,
            R.id.btnCapsula8CapsulasUsuarioSergio
        )

        idsCapsulas.forEach { id ->
            val capsula = findViewById<ImageButton>(id)
            capsula.isClickable = true
            capsula.isEnabled = true

            capsula.setOnClickListener {
                val status = (capsula.tag as? String)?.lowercase() ?: "indefinido"
                when (status) {
                    "verde" -> startActivity(Intent(this, CapsulaDisponivelUsuarioActivity::class.java))
                    "vermelha" -> startActivity(Intent(this, CapsulaIndisponivelUsuarioActivity::class.java))
                    "amarela", "amarelo", "manutencao", "manutenção" ->
                        startActivity(Intent(this, CapsulaManutencaoUsuarioActivity::class.java))
                    else -> startActivity(Intent(this, CapsulaIndisponivelUsuarioActivity::class.java))
                }
            }
        }
    }
}
