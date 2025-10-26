package com.example.biblifor

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FavoritosUsuarioActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_favoritos_usuario)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Navegação inferior
        findViewById<ImageView>(R.id.leoLogoHome3).setOnClickListener {
            startActivity(Intent(this, MenuPrincipalUsuarioActivity::class.java))
        }
        findViewById<ImageView>(R.id.leoImagemChatbot3).setOnClickListener {
            startActivity(Intent(this, ChatbotUsuarioActivity::class.java))
        }
        findViewById<ImageView>(R.id.leoImagemNotificacoes3).setOnClickListener {
            startActivity(Intent(this, AvisosUsuarioActivity::class.java))
        }
        findViewById<ImageView>(R.id.leoImagemMenu3).setOnClickListener {
            startActivity(Intent(this, MenuHamburguerUsuario::class.java))
        }

        // ===== RecyclerView =====
        val rv = findViewById<RecyclerView>(R.id.rvFavoritos)
        rv.layoutManager = LinearLayoutManager(this)

        // Lista de livros favoritos
        val favoritos = listOf(
            HistoryBook(
                title = "Dom Casmurro",
                author = "Machado de Assis",
                coverRes = R.drawable.livro_dom_casmurro,
                availabilityText = "Disponível: físico",
                isAvailable = true,
                dateText = "Favoritado hoje"
            ),
            HistoryBook(
                title = "1984",
                author = "George Orwell",
                coverRes = R.drawable.livro_1984,
                availabilityText = "Disponível: físico",
                isAvailable = true,
                dateText = "Favoritado ontem"
            ),
            HistoryBook(
                title = "Guerra e Paz",
                author = "L. Tolstói",
                coverRes = R.drawable.livro_guerra_e_paz,
                availabilityText = "Disponível: físico + digital",
                isAvailable = true,
                dateText = "Favoritado esta semana"
            ),
            HistoryBook(
                title = "Turma monica",
                author = "F. Kafka",
                coverRes = R.drawable.livro_turmamonica,
                availabilityText = "Indisponível",
                isAvailable = false,
                dateText = "Favoritado este mês"
            ),
            HistoryBook(
                title = "Código Limpo",
                author = "Robert C. Martin",
                coverRes = R.drawable.livro_cleancode,
                availabilityText = "Disponível: físico + digital",
                isAvailable = true,
                dateText = "Favoritado este mês"
            )
        )

        // Adapter com ação de clique
        rv.adapter = HistoryAdapter(favoritos) { livro ->
            if (livro.title == "teste teste") {
                startActivity(Intent(this, LivroDesfavoritadoUsuarioActivity::class.java))
            }
        }
    }
}
