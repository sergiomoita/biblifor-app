package com.example.biblifor

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class DisponiveisRenovacaoUsuarioActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_disponiveis_renovacao_usuario)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val imagemLivro = findViewById<ImageView>(R.id.lopesRomeuJulietaTeste)
        imagemLivro.setOnClickListener {

            val navegarLivrosRenovaveis = Intent(this, RenovacaoEmprestimoUsuarioActivity::class.java)
            startActivity(navegarLivrosRenovaveis)

        }

    }
}