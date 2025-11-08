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

class FavoritosUsuarioActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_favoritos_usuario)

        // Ajuste para edge-to-edge (modo imersivo)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // =============================
        // 🧭 Navegação Inferior
        // =============================
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

        // =============================
        // ⬆️ Ícones Superiores
        // =============================
        findViewById<ImageView>(R.id.lopesSetaVoltar32).setOnClickListener {
            finish()
        }
        findViewById<ImageView>(R.id.lopesChatBot32).setOnClickListener {
            startActivity(Intent(this, ChatbotUsuarioActivity::class.java))
        }
        findViewById<ImageView>(R.id.lopesNotificacao32).setOnClickListener {
            startActivity(Intent(this, AvisosUsuarioActivity::class.java))
        }

        // =============================
        // 📚 RecyclerView de Favoritos
        // =============================
        val rv = findViewById<RecyclerView>(R.id.rvFavoritos)
        rv.layoutManager = LinearLayoutManager(this)

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
                title = "O quinze",
                author = "Rachel Queiroz",
                coverRes = R.drawable.livro_rachelqueiroz,
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
                title = "Turma Monica",
                author = "M. Sousa",
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

        // Adapter com clique nos livros
        rv.adapter = HistoryAdapter(favoritos) { livro ->
            if (livro.title.equals("Turma Monica", ignoreCase = true) || !livro.isAvailable) {
                startActivity(Intent(this, LivroDesfavoritadoUsuarioActivity::class.java))
            } else {
                // Aqui poderia abrir detalhes do livro, se quiser futuramente
                // startActivity(Intent(this, DetalheLivroUsuarioActivity::class.java))
            }
        }
    }
}
