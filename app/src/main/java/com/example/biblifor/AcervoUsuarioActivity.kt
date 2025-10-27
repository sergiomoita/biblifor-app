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

class AcervoUsuarioActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_acervo_usuario)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Navegação inferior (mantida)
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

        // === RecyclerView (2 colunas) ===
        val rv = findViewById<RecyclerView>(R.id.rvAcervo)
        rv.layoutManager = GridLayoutManager(this, 2)

        // ORDEM conforme a tela que você mostrou:
        val acervo = listOf(
            RecommendedBook(
                title = "Código Limpo - Robert C.",
                coverRes = R.drawable.livro_cleancode,        // ajuste o nome do drawable se necessário
                availabilityText = "Disponível: físico + digital",
                available = true
            ),
            RecommendedBook(
                title = "Quincas Borba - M. de Assis",
                coverRes = R.drawable.livro_quincas,      // ajuste se o nome do arquivo for outro
                availabilityText = "Indisponível",
                available = false
            ),
            RecommendedBook(
                title = "Antígona - Sófocles",
                coverRes = R.drawable.livro_antigona,           // ajuste se necessário
                availabilityText = "Disponível: digital",
                available = true
            ),
            RecommendedBook(
                title = "TCC  Eletrônica Aplicada - M. Lopes",
                coverRes = R.drawable.livro_tcc,     // ajuste se necessário
                availabilityText = "Disponível: físico",
                available = true
            ),
            // Itens extras pra completar a grade (opcional)
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
            )
        )

        // Clique opcional: manter o mesmo comportamento do “Recomendados” (ex.: abrir popup em “Guerra e Paz”)
        rv.adapter = RecommendedAdapter(acervo) { book ->
            if (book.title.contains("Guerra e Paz", ignoreCase = true)) {
                startActivity(Intent(this, PopupEmprestimoProibidoUsuarioActivity::class.java))
            }
        }
    }
}
