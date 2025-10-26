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

class AvaliacaoUsuarioActivity : AppCompatActivity() {

    private lateinit var estrelas: List<ImageView>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_avaliacao_usuario)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // --- Referências do layout ---
        val estrela1 = findViewById<ImageView>(R.id.imageViewEstrela21)
        val estrela2 = findViewById<ImageView>(R.id.imageViewEstrela22)
        val estrela3 = findViewById<ImageView>(R.id.imageViewEstrela23)
        val estrela4 = findViewById<ImageView>(R.id.imageViewEstrela24)
        val estrela5 = findViewById<ImageView>(R.id.imageViewEstrela25)
        estrelas = listOf(estrela1, estrela2, estrela3, estrela4, estrela5)

        // Estado inicial das estrelas
        estrelas.forEach {
            it.setImageResource(R.drawable.estrela_vazia)
            it.tag = "apagada"
        }

        configurarEstrelas()

        val setaMenu = findViewById<ImageView>(R.id.imageViewSetaMenuGustavo)
        val chatMoema = findViewById<ImageView>(R.id.imageViewChatBotMoema)
        val avisos = findViewById<ImageView>(R.id.imageViewAvisos)

        setaMenu.setOnClickListener {
            startActivity(Intent(this, MenuPrincipalUsuarioActivity::class.java))
        }

        chatMoema.setOnClickListener {
            startActivity(Intent(this, ChatbotUsuarioActivity::class.java))
        }

        avisos.setOnClickListener {
            startActivity(Intent(this, AvisosUsuarioActivity::class.java))
        }

        val botaoEnviar = findViewById<Button>(R.id.button3)
        botaoEnviar.setOnClickListener {
            val nota = estrelas.indexOfLast { it.tag == "acesa" } + 1
            Toast.makeText(this, "Você deu $nota estrelas!", Toast.LENGTH_SHORT).show()
        }

        // --- Novo botão Voltar ao Menu ---
        val botaoVoltarMenu = findViewById<Button>(R.id.buttonVoltarMenu)
        botaoVoltarMenu.setOnClickListener {
            startActivity(Intent(this, MenuPrincipalUsuarioActivity::class.java))
            finish()
        }
    }

    private fun configurarEstrelas() {
        estrelas.forEachIndexed { index, estrela ->
            estrela.setOnClickListener { acenderEstrelas(index) }
        }
    }

    private fun acenderEstrelas(quantas: Int) {
        for (i in estrelas.indices) {
            if (i <= quantas) {
                estrelas[i].setImageResource(R.drawable.estrela_dourada)
                estrelas[i].tag = "acesa"
            } else {
                estrelas[i].setImageResource(R.drawable.estrela_vazia)
                estrelas[i].tag = "apagada"
            }
        }
    }
}
