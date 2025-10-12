package com.example.biblifor

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class LivroCadastrandoAdministradorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_livro_cadastrando_administrador)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val imgSeta = findViewById<ImageView>(R.id.lopesSetaVoltar39)
        imgSeta.setOnClickListener {
            val navegarSeta = Intent(this, CadastrarLivroAdministradorActivity::class.java)
            startActivity(navegarSeta)
        }

        val btnCadastrar = findViewById<Button>(R.id.lopesBtnCadastrar39)
        btnCadastrar.setOnClickListener {
            val navegarExcecoes = Intent(this, ConfirmacaoCadastroAdministradorActivity::class.java)
            startActivity(navegarExcecoes)
        }


    }
}