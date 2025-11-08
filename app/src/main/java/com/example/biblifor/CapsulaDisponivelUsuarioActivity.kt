package com.example.biblifor

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class CapsulaDisponivelUsuarioActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_capsula_disponivel_usuario)

        // 🔙 Botão de voltar
        val btnVoltar = findViewById<ImageView>(R.id.btnVoltarCapsulaDisponivelUsuarioSergio)
        btnVoltar.setOnClickListener {
            finish()
        }

        // 🎓 Botão "Reservar"
        val btnReservar = findViewById<Button>(R.id.btnAcessarLoginUsuarioSergio)
        btnReservar.setOnClickListener {
            mostrarToastReserva()
            finish() // volta para a tela anterior
        }
    }

    // 💬 Toast personalizado
    private fun mostrarToastReserva() {
        val inflater = LayoutInflater.from(this)
        val layout = inflater.inflate(R.layout.toast_sucesso_reserva, null)

        val textView = layout.findViewById<TextView>(R.id.tvMensagemToastSucesso)
        textView.text = "Cápsula de estudo reservada com sucesso!"

        with(Toast(this)) {
            duration = Toast.LENGTH_SHORT
            view = layout
            show()
        }
    }
}