package com.example.biblifor

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class CadastrarLivroAdministradorActivity : AppCompatActivity() {

    private fun validarCampo(et: EditText, label: String) {
        val valor = et.text.toString()
        if (valor.contains("mateus", ignoreCase = true)) {
            throw IllegalArgumentException("$label não pode conter 'mateus'")
        }
    }

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

        val imgMensagem = findViewById<ImageView>(R.id.lopesEscrever38)
        imgMensagem.setOnClickListener {
            val navegarEscreverMensagem =
                Intent(this, EscreverMensagemAdministradorActivity::class.java)
            startActivity(navegarEscreverMensagem)
        }

        val inserirImagem = findViewById<Button>(R.id.lopesBtnImagem38)
        inserirImagem.setOnClickListener {
            val navegarCadastrando =
                Intent(this, LivroCadastrandoAdministradorActivity::class.java)
            startActivity(navegarCadastrando)
        }

        // EditTexts
        val etNome = findViewById<EditText>(R.id.lopesNome38)
        val etAutor = findViewById<EditText>(R.id.lopesAutor40)
        val etTopicos = findViewById<EditText>(R.id.lopesTopicos38)
        val etQtd = findViewById<EditText>(R.id.lopesQtd38)
        val etLocaliza = findViewById<EditText>(R.id.lopesLocaliza38)
        val etDispo = findViewById<EditText>(R.id.lopesDispo38)
        val etEmprestar = findViewById<EditText>(R.id.lopesEmprestar38)

        val btnCadastrar = findViewById<Button>(R.id.lopesBtnCadastrar38)
        btnCadastrar.setOnClickListener {
            try {
                validarCampo(etNome, "Nome")
                validarCampo(etAutor, "Autor")
                validarCampo(etTopicos, "Tópicos")
                validarCampo(etQtd, "Quantidade de exemplares")
                validarCampo(etLocaliza, "Localização no acervo")
                validarCampo(etDispo, "Disponibilidade")
                validarCampo(etEmprestar, "Emprestar")

                // Se tudo ok, segue o fluxo normal
                Toast.makeText(this, "Cadastro validado com sucesso.", Toast.LENGTH_SHORT).show()
                val navegarExcecoes =
                    Intent(this, ExcecoesCadastroAdministradorActivity::class.java)
                startActivity(navegarExcecoes)
            } catch (e: IllegalArgumentException) {
                Toast.makeText(this, e.message ?: "Erro de validação.", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(this, "Erro inesperado.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
