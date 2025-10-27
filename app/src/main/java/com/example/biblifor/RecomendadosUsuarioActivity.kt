package com.example.biblifor

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RecomendadosUsuarioActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_recomendados_usuario)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<ImageView>(R.id.btnVoltarRecomendados).setOnClickListener {
            finish() // volta para a tela anterior
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
            startActivity(Intent(this, MenuHamburguerUsuarioActivity::class.java))
        }

        // ===== RecyclerView (2 colunas) =====
        val rv = findViewById<RecyclerView>(R.id.rvRecomendados)
        rv.layoutManager = GridLayoutManager(this, 2)

        val recomendados = listOf(
            RecommendedBook(
                title = "Guerra e Paz - L. Tolstói",
                coverRes = R.drawable.livro_guerra_e_paz,
                availabilityText = "Disponível: físico + digital",
                available = true
            ),
            RecommendedBook(
                title = "A Metamorfose - F. Kafka",
                coverRes = R.drawable.livro_metamorfose,
                availabilityText = "Indisponível",
                available = false
            ),
            RecommendedBook(
                title = "Romeu e Julieta - W. Carrasco",
                coverRes = R.drawable.livro_romeu1,
                availabilityText = "Disponível: digital",
                available = true
            ),
            RecommendedBook(
                title = "Dom Casmurro - M. de Assis",
                coverRes = R.drawable.livro_dom_casmurro,
                availabilityText = "Disponível: físico",
                available = true
            ),
            RecommendedBook(
                title = "Ilíada - Homero",
                coverRes = R.drawable.livro_iliada,
                availabilityText = "Indisponível",
                available = false
            ),
            RecommendedBook(
                title = "O livro dos Juízes",
                coverRes = R.drawable.livro_dos_juizes,
                availabilityText = "Disponível: físico + digital",
                available = true
            )
        )

        // Clique nos livros
        rv.adapter = RecommendedAdapter(recomendados) { book ->
            when {
                book.title.contains("Guerra e Paz", ignoreCase = true) -> {
                    startActivity(Intent(this, PopupEmprestimoProibidoUsuarioActivity::class.java))
                }
                book.title.contains("Romeu e Julieta", ignoreCase = true) -> {
                    startActivity(Intent(this, PopupLivroOnlineUsuarioActivity::class.java))
                }
            }
        }
    }
}
