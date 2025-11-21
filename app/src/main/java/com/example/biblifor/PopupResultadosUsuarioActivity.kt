package com.example.biblifor

import android.graphics.BitmapFactory
import android.util.Base64
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class PopupResultadosUsuarioActivity : BaseActivity() {

    private var isFavorito = false
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_popup_resultados_usuario)

        // ============================
        // RECEBER DADOS DO INTENT
        // ============================
        val livroId = intent.getStringExtra("livroId") ?: ""   // 👈 ID REAL DO DOCUMENTO
        val titulo = intent.getStringExtra("titulo") ?: "Título indisponível"
        val autor = intent.getStringExtra("autor") ?: ""
        val situacao = intent.getStringExtra("situacao") ?: ""
        val disponibilidade = intent.getStringExtra("disponibilidade") ?: ""
        val imagemBase64 = intent.getStringExtra("imagemBase64")
        val matricula = getSharedPreferences("APP_PREFS", MODE_PRIVATE)
            .getString("MATRICULA_USER", "") ?: ""


        // Views
        val txtTitulo = findViewById<TextView>(R.id.txtTituloPopupResultadosUsuario)
        val txtStatus = findViewById<TextView>(R.id.txtStatusPopupResultadosUsuario)
        val imgCapa = findViewById<ImageView>(R.id.imgCapaPopupResultadosUsuario)
        val iconFavorito = findViewById<ImageView>(R.id.iconFavoritoPopupResultadosUsuario)

        // Nome do livro
        txtTitulo.text = if (autor.isNotEmpty()) "$titulo\n($autor)" else titulo

        // Status
        val statusFinal = when {
            disponibilidade.contains("Físico") && disponibilidade.contains("Online") ->
                "Disponível em mídia física e digital"
            disponibilidade.contains("Físico") ->
                "Disponível somente em mídia física"
            disponibilidade.contains("Online") ->
                "Disponível somente online"
            else -> "Indisponível"
        }
        txtStatus.text = statusFinal

        // ============================
        // CARREGAR IMAGEM BASE64
        // ============================
        if (!imagemBase64.isNullOrEmpty()) {
            try {
                val bytes = Base64.decode(imagemBase64, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                imgCapa.setImageBitmap(bitmap)
            } catch (e: Exception) {
                imgCapa.setImageResource(R.drawable.livro_rachelqueiroz)
            }
        } else {
            imgCapa.setImageResource(R.drawable.livro_rachelqueiroz)
        }

        // ============================
        // CHECAR SE JÁ É FAVORITO
        // ============================
        if (livroId.isNotEmpty() && matricula.isNotEmpty()) {
            db.collection("alunos")
                .document(matricula)
                .collection("favoritos")
                .document(livroId)
                .get()
                .addOnSuccessListener { doc ->
                    isFavorito = doc.exists()
                    iconFavorito.setImageResource(
                        if (isFavorito) R.drawable.favoritado else R.drawable.desfavoritado
                    )
                }
        }

        // ============================
        // FAVORITAR / DESFAVORITAR
        // ============================
        iconFavorito.setOnClickListener {

            if (livroId.isEmpty() || matricula.isEmpty()) {
                Toast.makeText(this, "Erro ao favoritar.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            isFavorito = !isFavorito

            if (isFavorito) {
                // FAVORITAR
                db.collection("alunos")
                    .document(matricula)
                    .collection("favoritos")
                    .document(livroId)
                    .set(mapOf("favorito" to true))
                    .addOnSuccessListener {
                        iconFavorito.setImageResource(R.drawable.favoritado)
                        Toast.makeText(this, "Adicionado aos favoritos", Toast.LENGTH_SHORT).show()
                    }

            } else {
                // DESFAVORITAR
                db.collection("alunos")
                    .document(matricula)
                    .collection("favoritos")
                    .document(livroId)
                    .delete()
                    .addOnSuccessListener {
                        iconFavorito.setImageResource(R.drawable.desfavoritado)
                        Toast.makeText(this, "Removido dos favoritos", Toast.LENGTH_SHORT).show()
                    }
            }
        }

        // ============================
        // BOTÃO VOLTAR
        // ============================
        findViewById<ImageView>(R.id.btnVoltarPopupResultadosUsuarioSergio)
            .setOnClickListener { finish() }

        // ============================
        // BOTÕES PRINCIPAIS
        // ============================
        findViewById<Button>(R.id.btnEmprestimoPopupResultadosUsuario).setOnClickListener {

            if (livroId.isEmpty()) {
                Toast.makeText(this, "Erro ao carregar o livro.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Buscar unidades no Firestore ANTES de abrir a tela
            db.collection("livros")
                .document(livroId)
                .get()
                .addOnSuccessListener { doc ->

                    val unidades = doc.getLong("quantidade")?.toInt() ?: 0

                    val intent = Intent(this, EmprestimoUsuarioActivity::class.java)

                    intent.putExtra("livroId", livroId)
                    intent.putExtra("titulo", titulo)
                    intent.putExtra("autor", autor)
                    intent.putExtra("imagemBase64", imagemBase64)
                    intent.putExtra("situacao", situacao)
                    intent.putExtra("disponibilidade", disponibilidade)
                    intent.putExtra("unidades", unidades)   // << AQUI!

                    startActivity(intent)
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Erro ao carregar dados do livro.", Toast.LENGTH_SHORT).show()
                }
        }



        findViewById<Button>(R.id.btnOnlinePopupResultadosUsuario).setOnClickListener {
            Toast.makeText(this, "Abrindo versão online...", Toast.LENGTH_SHORT).show()
        }

        findViewById<Button>(R.id.btnFavoritosPopupResultadosUsuario).setOnClickListener {
            startActivity(Intent(this, FavoritosUsuarioActivity::class.java))
        }

        // ============================
        // BARRA INFERIOR
        // ============================
        findViewById<ImageView>(R.id.iconHomePopupResultadosUsuario).setOnClickListener {
            startActivity(Intent(this, MenuPrincipalUsuarioActivity::class.java))
        }

        findViewById<ImageView>(R.id.iconChatBotPopupResultadosUsuario).setOnClickListener {
            startActivity(Intent(this, ChatbotUsuarioActivity::class.java))
        }

        findViewById<ImageView>(R.id.iconMensagemPopupResultadosUsuario).setOnClickListener {
            startActivity(Intent(this, AvisosUsuarioActivity::class.java))
        }

        findViewById<ImageView>(R.id.iconMenuPopupResultadosUsuario).setOnClickListener {
            startActivity(Intent(this, MenuHamburguerUsuarioActivity::class.java))
        }
    }
}
