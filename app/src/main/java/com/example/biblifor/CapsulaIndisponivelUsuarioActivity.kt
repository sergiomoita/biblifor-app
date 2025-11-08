package com.example.biblifor

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class CapsulaIndisponivelUsuarioActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_capsula_indisponivel_usuario) // XML correspondente

        // 🔙 Seta de voltar → CapsulasUsuarioActivity
        val btnVoltar = findViewById<ImageView>(R.id.btnVoltarCapsulaIndisponivelUsuarioSergio)
        btnVoltar.setOnClickListener {
            val intent = Intent(this, CapsulasUsuarioActivity::class.java)
            startActivity(intent)
            finish()
        }

        // 🔴 Botão “Indisponível” → CapsulasUsuarioActivity
        val btnIndisponivel = findViewById<Button>(R.id.btnCapsulaIndisponivelUsuarioSergio)
        btnIndisponivel.setOnClickListener {
            val intent = Intent(this, CapsulasUsuarioActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}