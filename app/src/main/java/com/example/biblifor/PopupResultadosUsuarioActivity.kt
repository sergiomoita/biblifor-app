package com.example.biblifor

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
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
        val livroId = intent.getStringExtra("livroId") ?: ""
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
        val btnEmprestimo = findViewById<Button>(R.id.btnEmprestimoPopupResultadosUsuario)
        val btnOnline = findViewById<Button>(R.id.btnOnlinePopupResultadosUsuario)

        // Nome do livro
        txtTitulo.text = if (autor.isNotEmpty()) "$titulo\n($autor)" else titulo

        // Status geral de exibição
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
        // LÓGICA DE DISPONIBILIDADE / SITUAÇÃO
        // ============================
        val hasFisico = disponibilidade.contains("Físico", ignoreCase = true)
        val hasOnline = disponibilidade.contains("Online", ignoreCase = true)
        val situacaoEmprestimo = situacao // "Emprestável" ou "Não-emprestável"

        // ----- CONFIGURAR BOTÃO EMPRÉSTIMO -----
        var podeEmprestar = false
        if (hasFisico && situacaoEmprestimo == "Emprestável") {
            // pode emprestar normalmente
            podeEmprestar = true
            btnEmprestimo.isEnabled = true
            btnEmprestimo.alpha = 1f
            btnEmprestimo.text = "Empréstimo"
        } else if (hasFisico && situacaoEmprestimo == "Não-emprestável") {
            // tem mídia física, mas não é emprestável
            btnEmprestimo.isEnabled = false
            btnEmprestimo.alpha = 0.6f
            btnEmprestimo.text = "Indisponível"
        } else if (!hasFisico) {
            // não tem mídia física
            btnEmprestimo.isEnabled = false
            btnEmprestimo.alpha = 0.6f
            btnEmprestimo.text = "Indisponível"
        } else {
            // qualquer outra situação inesperada
            btnEmprestimo.isEnabled = false
            btnEmprestimo.alpha = 0.6f
            btnEmprestimo.text = "Indisponível"
        }

        // Só configura o clique se REALMENTE puder emprestar
        if (podeEmprestar) {
            btnEmprestimo.setOnClickListener {

                if (livroId.isEmpty()) {
                    Toast.makeText(this, "Erro ao carregar o livro.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

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
                        intent.putExtra("situacao", situacaoEmprestimo)
                        intent.putExtra("disponibilidade", disponibilidade)
                        intent.putExtra("unidades", unidades)
                        startActivity(intent)
                    }
                    .addOnFailureListener {
                        Toast.makeText(
                            this,
                            "Erro ao carregar dados do livro.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }
        } else {
            // Garante que nenhum clique funcione se estiver desativado
            btnEmprestimo.setOnClickListener { }
        }

        // ----- CONFIGURAR BOTÃO ONLINE -----
        if (hasOnline) {
            btnOnline.isEnabled = true
            btnOnline.alpha = 1f
            btnOnline.text = "Online"

            btnOnline.setOnClickListener {
                val uri = Uri.parse("https://biblioteca.sophia.com.br/terminal/9575")
                val intent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(intent)
            }
        } else {
            btnOnline.isEnabled = false
            btnOnline.alpha = 0.6f
            btnOnline.text = "Indisponível"
            btnOnline.setOnClickListener { }
        }

        // ============================
        // BOTÃO VOLTAR
        // ============================
        findViewById<ImageView>(R.id.btnVoltarPopupResultadosUsuarioSergio)
            .setOnClickListener { finish() }

        // ============================
        // BOTÃO FAVORITOS
        // ============================
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
