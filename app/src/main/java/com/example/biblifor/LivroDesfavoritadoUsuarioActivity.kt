package com.example.biblifor

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LivroDesfavoritadoUsuarioActivity : AppCompatActivity() {

    private var isFavorito = true // Estado do ícone de favorito

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_livro_desfavoritado_usuario)

        // --- Botão Voltar ---
        val btnVoltar = findViewById<ImageView>(R.id.btnVoltarLivroDesfavoritadoUsuario)
        btnVoltar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // --- Ícone de Favorito ---
        val iconFavorito = findViewById<ImageView>(R.id.iconFavoritoLivroDesfavoritadoUsuario)
        iconFavorito.setOnClickListener {
            isFavorito = !isFavorito

            if (isFavorito) {
                iconFavorito.setImageResource(R.drawable.favoritado)
                mostrarToastPersonalizado(
                    "Livro adicionado aos favoritos",
                    "Corpo Humano – Turma da Mônica",
                    true
                )
            } else {
                iconFavorito.setImageResource(R.drawable.desfavoritado)
                mostrarToastPersonalizado(
                    "Livro removido dos favoritos",
                    "Corpo Humano – Turma da Mônica",
                    false
                )
            }
        }

        // --- Botão para ver livros favoritos atualizados ---
        val btnVoltarFavoritos = findViewById<Button>(R.id.btnFavoritosAtualizado)
        btnVoltarFavoritos.setOnClickListener {
            startActivity(Intent(this, FavoritosAtualizadoActivity::class.java))
            finish()
        }

        // --- Barra inferior ---
        val iconHome = findViewById<ImageView>(R.id.iconHomeLivroDesfavoritadoUsuario)
        val iconChatbot = findViewById<ImageView>(R.id.iconChatbotLivroDesfavoritadoUsuario)
        val iconMensagem = findViewById<ImageView>(R.id.iconMensagemLivroDesfavoritadoUsuario)
        val iconMenu = findViewById<ImageView>(R.id.iconMenuLivroDesfavoritadoUsuario)

        iconHome.setOnClickListener {
            startActivity(Intent(this, MenuPrincipalUsuarioActivity::class.java))
            finish()
        }

        iconChatbot.setOnClickListener {
            startActivity(Intent(this, ChatbotUsuarioActivity::class.java))
        }

        iconMensagem.setOnClickListener {
            startActivity(Intent(this, AvisosUsuarioActivity::class.java))
        }

        iconMenu.setOnClickListener {
            startActivity(Intent(this, MenuPrincipalUsuarioActivity::class.java))
            finish()
        }
    }

    // =====================================
    // 🔔 Função para mostrar Toast customizado
    // =====================================
    private fun mostrarToastPersonalizado(titulo: String, subtitulo: String, adicionou: Boolean) {
        val inflater = LayoutInflater.from(this)
        val layout = inflater.inflate(R.layout.toast_custom_livro, null)

        val txtTitulo = layout.findViewById<TextView>(R.id.toastTitulo)
        val txtSubtitulo = layout.findViewById<TextView>(R.id.toastSubtitulo)
        val iconStatus = layout.findViewById<ImageView>(R.id.toastIconStatus)
        val layoutFundo = layout.findViewById<LinearLayout>(R.id.toastFundo)

        txtTitulo.text = titulo
        txtSubtitulo.text = subtitulo

        layoutFundo.setBackgroundResource(R.drawable.bg_toast_branco)
        if (adicionou) {
            iconStatus.setImageResource(R.drawable.check_verde)
        } else {
            iconStatus.setImageResource(R.drawable.desfavoritado)
        }

        val toast = Toast(this)
        toast.duration = Toast.LENGTH_SHORT
        toast.view = layout
        toast.setGravity(Gravity.TOP or Gravity.END, 20, 150)
        toast.show()
    }
}
