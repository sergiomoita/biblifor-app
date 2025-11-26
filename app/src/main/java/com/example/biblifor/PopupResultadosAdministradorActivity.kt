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

class PopupResultadosAdministradorActivity : BaseActivity() {

    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_popup_resultados_administrador)

        val livroId = intent.getStringExtra("livroId") ?: ""

        val btnVoltar = findViewById<ImageView>(R.id.btnVoltarPopupAdm)
        val imgCapa = findViewById<ImageView>(R.id.imgCapaPopupAdm)
        val txtTitulo = findViewById<TextView>(R.id.txtTituloPopupAdm)
        val txtStatus = findViewById<TextView>(R.id.txtStatusPopupAdm)
        val btnEditar = findViewById<TextView>(R.id.btnEditarLivroPopupAdm)
        val btnDeletar = findViewById<TextView>(R.id.btnDeletarLivroPopupAdm)

        // ==== Barra inferior (SEM finish!!) ====
        findViewById<ImageView>(R.id.iconHomePopupAdm).setOnClickListener {
            startActivity(Intent(this, MenuPrincipalAdministradorActivity::class.java))
        }

        findViewById<ImageView>(R.id.iconEscreverMensagemPopupAdm).setOnClickListener {
            startActivity(Intent(this, EscreverMensagemAdministradorActivity::class.java))
        }

        findViewById<ImageView>(R.id.iconMensagemPopupAdm).setOnClickListener {
            startActivity(Intent(this, MensagensAdministradorActivity::class.java))
        }

        findViewById<ImageView>(R.id.iconMenuPopupAdm).setOnClickListener {
            startActivity(Intent(this, MenuHamburguerAdministradorActivity::class.java))
        }

        // ==== Botão voltar ====
        btnVoltar.setOnClickListener { finish() }

        if (livroId.isNotEmpty()) {
            carregarDadosLivro(livroId, imgCapa, txtTitulo, txtStatus)
        }

        btnEditar.setOnClickListener {
            val intent = Intent(this, EditarLivroAdministradorActivity::class.java)
            intent.putExtra("livroId", livroId)
            startActivity(intent)
        }

        // ==== Botão deletar ====
        btnDeletar.setOnClickListener {
            mostrarPopupConfirmacao(livroId)
        }
    }

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
                val disponibilidade = doc.getString("Disponibilidade") ?: ""
                val imagemBase64 = doc.getString("Imagem")

                txtTitulo.text =
                    if (autor.isNotEmpty()) "$titulo\n($autor)" else titulo

                txtStatus.text = disponibilidade

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

    private fun mostrarPopupConfirmacao(livroId: String) {
        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Confirmar exclusão")
        builder.setMessage("Você realmente deseja deletar este livro?")

        builder.setPositiveButton("Sim") { _, _ ->
            deletarLivro(livroId)
        }

        builder.setNegativeButton("Não") { dialog, _ ->
            dialog.dismiss()
        }

        builder.create().show()
    }

    private fun deletarLivro(livroId: String) {
        db.collection("livros")
            .document(livroId)
            .delete()
            .addOnSuccessListener {
                val intent = Intent(this, LivrosEmprestaveisAdministradorActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener {
                android.widget.Toast.makeText(this, "Erro ao deletar o livro.", android.widget.Toast.LENGTH_SHORT).show()
            }
    }
}
