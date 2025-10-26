package com.example.biblifor

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PopupEmprestimoProibidoUsuarioActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_popup_emprestimo_proibido_usuario)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnEmprestimo = findViewById<Button>(R.id.ButtonAvaliarGustavo03)
        val btnOnline = findViewById<Button>(R.id.buttonOnline01Gustavo)
        val btnSair = findViewById<ImageView>(R.id.btnSair)

        // Clique em "Emprestimo" -> muda o texto do botão para "Proibido" e mostra Toast
        btnEmprestimo.setOnClickListener {
            btnEmprestimo.text = "Proibido"
            Toast.makeText(
                this,
                "midia fisica restrita a biblioteca",
                Toast.LENGTH_SHORT
            ).show()
        }

        // Clique em "Online" -> vai para a tela de Limite de Empréstimos
        btnOnline.setOnClickListener {
            val intent = Intent(this, LimiteEmprestimosUsuarioActivity::class.java)
            startActivity(intent)
        }

        btnSair.setOnClickListener {
            val navegar = Intent(this, RecomendadosUsuarioActivity::class.java)
            startActivity(navegar)
        }
    }
}
