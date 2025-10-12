package com.example.biblifor

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class CadastrarLivroAdministradorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cadastrar_livro_administrador)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val imgSeta = findViewById<ImageView>(R.id.lopesSetaVoltar38)
        imgSeta.setOnClickListener {
            val navegarSeta = Intent(this, MenuPrincipalAdministradorActivity::class.java)
            startActivity(navegarSeta)
        }

        val  imgMensagem = findViewById<ImageView>(R.id.lopesEscrever38)
        imgMensagem.setOnClickListener {
            val navegarEscreverMensagem = Intent(this, EscreverMensagemAdministradorActivity::class.java)
            startActivity(navegarEscreverMensagem)
        }

        val inserirImagem = findViewById<Button>(R.id.lopesBtnImagem38)
        inserirImagem.setOnClickListener {
            val navegarCadastrando = Intent(this, LivroCadastrandoAdministradorActivity::class.java)
            startActivity(navegarCadastrando)
        }

        val btnCadastrar = findViewById<Button>(R.id.lopesBtnCadastrar38)
        btnCadastrar.setOnClickListener {
            val navegarExcecoes = Intent(this, ExcecoesCadastroAdministradorActivity::class.java)
            startActivity(navegarExcecoes)
        }

    }
}