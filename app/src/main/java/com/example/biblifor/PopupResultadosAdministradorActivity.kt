package com.example.biblifor

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class PopupResultadosAdministradorActivity : AppCompatActivity() {

    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_popup_resultados_administrador)

        // ---- Receber o ID do livro da tela anterior ----
        val livroId = intent.getStringExtra("livroId") ?: ""

        // ---- Views ----
        val btnVoltar = findViewById<ImageView>(R.id.btnVoltarPopupAdm)
        val imgCapa = findViewById<ImageView>(R.id.imgCapaPopupAdm)
        val txtTitulo = findViewById<TextView>(R.id.txtTituloPopupAdm)
        val txtStatus = findViewById<TextView>(R.id.txtStatusPopupAdm)
        val btnEditar = findViewById<TextView>(R.id.btnEditarLivroPopupAdm)

        // ---- Barra inferior ----
        findViewById<ImageView>(R.id.iconHomePopupAdm).setOnClickListener {
            startActivity(Intent(this, MenuPrincipalAdministradorActivity::class.java))
            finish()
        }

        findViewById<ImageView>(R.id.iconChatBotPopupAdm).setOnClickListener {
            startActivity(Intent(this, EscreverMensagemAdministradorActivity::class.java))
        }

        findViewById<ImageView>(R.id.iconMensagemPopupAdm).setOnClickListener {
            startActivity(Intent(this, MensagensAdministradorActivity::class.java))
        }

        findViewById<ImageView>(R.id.iconMenuPopupAdm).setOnClickListener {
            startActivity(Intent(this, MenuHamburguerAdministradorActivity::class.java))
        }

        // ---- Botão voltar ----
        btnVoltar.setOnClickListener { finish() }

        // ---- Carregar informações do livro ----
        if (livroId.isNotEmpty()) {
            carregarDadosLivro(livroId, imgCapa, txtTitulo, txtStatus)
        }

        // ---- Botão Editar Livro ----
        btnEditar.setOnClickListener {
            val intent = Intent(this, EditarLivroAdministradorActivity::class.java)
            intent.putExtra("livroId", livroId)
            startActivity(intent)
        }
    }


    // ======================================================
    //             CARREGAR DADOS DO LIVRO
    // ======================================================
    private fun carregarDadosLivro(
        livroId: String,
        imgCapa: ImageView,
        txtTitulo: TextView,
        txtStatus: TextView
    ) {
        db.collection("livros")
            .document(livroId)
            .get()
            .addOnSuccessListener { doc ->
                if (!doc.exists()) return@addOnSuccessListener

                val titulo = doc.getString("Titulo") ?: livroId
                val autor = doc.getString("Autor") ?: ""
                val situacao = doc.getString("SituacaoEmprestimo") ?: ""
                val disponibilidade = doc.getString("Disponibilidade") ?: ""
                val imagemBase64 = doc.getString("Imagem")

                // ---- Título ----
                txtTitulo.text =
                    if (autor.isNotEmpty()) "$titulo\n($autor)" else titulo

                // ---- Status (Disponibilidade) ----
                txtStatus.text = disponibilidade

                // ---- Capa ----
                if (!imagemBase64.isNullOrEmpty()) {
                    try {
                        val bytes = Base64.decode(imagemBase64, Base64.DEFAULT)
                        val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                        imgCapa.setImageBitmap(bmp)
                    } catch (_: Exception) {
                        imgCapa.setImageResource(R.drawable.livro_rachelqueiroz)
                    }
                } else {
                    imgCapa.setImageResource(R.drawable.livro_rachelqueiroz)
                }
            }
    }
}
