package com.example.biblifor

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.ImageView
import com.example.biblifor.R

class EsqueceuSenhaUsuarioActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_esqueceu_senha_usuario) // nome do seu XML

        // Referências aos elementos
        val btnVoltar = findViewById<ImageView>(R.id.btnVoltarEsqueceuSenhaUsuarioSergio)
        val btnAcessar = findViewById<Button>(R.id.btnAcessarEsqueceuSenhaUsuarioSergio)

        // Clique na seta → volta para tela de login
        btnVoltar.setOnClickListener {
            val intent = Intent(this, LoginUsuarioActivity::class.java)
            startActivity(intent)
            finish() // fecha a tela atual
        }

        // Clique em "Acessar" → vai para tela principal
        btnAcessar.setOnClickListener {
            val intent = Intent(this, MenuPrincipalUsuarioActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}