
package com.example.biblifor

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class EscreverMensagemAdministradorActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_escrever_mensagem_administrador)

        // 🔙 Botão Voltar
        val btnVoltar = findViewById<ImageView>(R.id.btnVoltarEscreverMensagemAdministradorSergio)
        btnVoltar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // ✉️ Campos da mensagem
        val etDestinatarios =
            findViewById<EditText>(R.id.etDestinatariosEscreverMensagemAdministradorSergio)
        val etAssunto =
            findViewById<EditText>(R.id.etAssuntoEscreverMensagemAdministradorSergio)
        val btnEnviar =
            findViewById<Button>(R.id.btnEnviarEscreverMensagemAdministradorSergio)

        // 📤 Botão Enviar
        btnEnviar.setOnClickListener {
            val destinatarios = etDestinatarios.text.toString().trim()
            val assunto = etAssunto.text.toString().trim()

            if (destinatarios.isEmpty() || assunto.isEmpty()) {
                mostrarToastBranco("Por favor, preencha todos os campos.")
            } else {
                mostrarToastBranco("Mensagem enviada com sucesso!")
                etDestinatarios.text.clear()
                etAssunto.text.clear()
            }
        }

        // ⚙️ Barra inferior
        val iconHome =
            findViewById<ImageView>(R.id.iconHomeEscreverMensagemAdministradorSergio)
        val iconEscrever =
            findViewById<ImageView>(R.id.iconEscreverEscreverMensagemAdministradorSergio)
        val iconMensagem =
            findViewById<ImageView>(R.id.iconMensagemEscreverMensagemAdministradorSergio)
        val iconMenu =
            findViewById<ImageView>(R.id.iconMenuInferiorEscreverMensagemAdministradorSergio)

        iconHome.setOnClickListener {
            val intent = Intent(this, MenuPrincipalAdministradorActivity::class.java)
            startActivity(intent)
            finish()
        }

        iconEscrever.setOnClickListener {
            val intent = Intent(this, EscreverMensagemAdministradorActivity::class.java)
            startActivity(intent)
            finish()
        }

        iconMensagem.setOnClickListener {
            val intent = Intent(this, MensagensAdministradorActivity::class.java)
            startActivity(intent)
        }

        iconMenu.setOnClickListener {
            val intent = Intent(this, MenuPrincipalAdministradorActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    // ================================
    // 🧡 Toast Branco Personalizado
    // ================================
    private fun mostrarToastBranco(mensagem: String) {
        val inflater = LayoutInflater.from(this)
        val layout = inflater.inflate(R.layout.toast_mensagem_enviada, null)

        val txtMensagem = layout.findViewById<TextView>(R.id.toast_text)
        txtMensagem.text = mensagem

        val toast = Toast(applicationContext)
        toast.duration = Toast.LENGTH_SHORT
        toast.view = layout
        toast.setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 200)
        toast.show()
    }
}

