package com.example.biblifor

import android.graphics.BitmapFactory
import android.util.Base64
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

class PopupResultadosUsuarioActivity : BaseActivity() {

    private var isFavorito = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_popup_resultados_usuario)

        // ==============================
        // RECEBENDO DADOS DO INTENT
        // ==============================

        val titulo = intent.getStringExtra("titulo") ?: "Título indisponível"
        val autor = intent.getStringExtra("autor") ?: ""
        val situacao = intent.getStringExtra("situacao") ?: ""
        val disponibilidade = intent.getStringExtra("disponibilidade") ?: ""
        val imagemBase64 = intent.getStringExtra("imagemBase64")

        // Views
        val txtTitulo = findViewById<TextView>(R.id.txtTituloPopupResultadosUsuario)
        val txtStatus = findViewById<TextView>(R.id.txtStatusPopupResultadosUsuario)
        val imgCapa = findViewById<ImageView>(R.id.imgCapaPopupResultadosUsuario)
        val iconFavorito = findViewById<ImageView>(R.id.iconFavoritoPopupResultadosUsuario)

        // Título
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

        // ==============================
        // Carregar capa Base64 → Bitmap
        // ==============================
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

        // ==============================
        // FAVORITAR
        // ==============================

        iconFavorito.setOnClickListener {
            isFavorito = !isFavorito

            if (isFavorito) {
                iconFavorito.setImageResource(R.drawable.favoritado)
                Toast.makeText(this, "Adicionado aos favoritos", Toast.LENGTH_SHORT).show()
            } else {
                iconFavorito.setImageResource(R.drawable.desfavoritado)
                Toast.makeText(this, "Removido dos favoritos", Toast.LENGTH_SHORT).show()
            }
        }

        // ==============================
        // BOTÃO VOLTAR
        // ==============================
        findViewById<ImageView>(R.id.btnVoltarPopupResultadosUsuarioSergio)
            .setOnClickListener { finish() }

        // ==============================
        // BOTÕES PRINCIPAIS
        // ==============================
        findViewById<Button>(R.id.btnEmprestimoPopupResultadosUsuario).setOnClickListener {
            startActivity(Intent(this, EmprestimoUsuarioActivity::class.java))
        }

        findViewById<Button>(R.id.btnOnlinePopupResultadosUsuario).setOnClickListener {
            Toast.makeText(this, "Abrindo versão online...", Toast.LENGTH_SHORT).show()
        }

        findViewById<Button>(R.id.btnFavoritosPopupResultadosUsuario).setOnClickListener {
            startActivity(Intent(this, FavoritosUsuarioActivity::class.java))
        }

        // ==============================
        // BARRA INFERIOR
        // ==============================

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
