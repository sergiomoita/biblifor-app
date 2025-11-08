package com.example.biblifor

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ConfirmacaoCadastroAdministradorActivity : BaseActivity() {

    private fun showToastConfirmado(msg: String) {
        val view = layoutInflater.inflate(R.layout.toast_cadastro_confirmado, null)
        view.findViewById<TextView>(R.id.toast_message_confirmado).text = msg
        Toast(this).apply {
            duration = Toast.LENGTH_SHORT
            setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 120)
            this.view = view
            show()
        }
    }

    private fun showToastCancelado(msg: String) {
        val view = layoutInflater.inflate(R.layout.toast_cadastro_cancelado, null)
        view.findViewById<TextView>(R.id.toast_message_cancelado).text = msg
        Toast(this).apply {
            duration = Toast.LENGTH_SHORT
            setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 120)
            this.view = view
            show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_confirmacao_cadastro_administrador)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val tvPergunta = findViewById<TextView>(R.id.lopesDeseja41)
        val btnNao = findViewById<Button>(R.id.lopesNao41)
        val btnSim = findViewById<Button>(R.id.lopesSim41)
        val btnInicio = findViewById<Button>(R.id.lopesInicio41)

        // Navegar para o início
        btnInicio.setOnClickListener {
            startActivity(Intent(this, MenuPrincipalAdministradorActivity::class.java))
            finish()
        }

        btnSim.setOnClickListener {
            // Confirmação
            tvPergunta.text = "Cadastro confirmado"
            showToastConfirmado("Cadastro confirmado")
            btnInicio.visibility = View.VISIBLE

            // Evita cliques repetidos
            btnSim.isEnabled = false
            btnNao.isEnabled = false
            btnSim.alpha = 0.6f
            btnNao.alpha = 0.6f
        }

        btnNao.setOnClickListener {
            // Cancelamento
            tvPergunta.text = "Cadastro cancelado"
            showToastCancelado("Cadastro cancelado")
            btnInicio.visibility = View.VISIBLE

            // Evita cliques repetidos
            btnSim.isEnabled = false
            btnNao.isEnabled = false
            btnSim.alpha = 0.6f
            btnNao.alpha = 0.6f
        }
    }
}
