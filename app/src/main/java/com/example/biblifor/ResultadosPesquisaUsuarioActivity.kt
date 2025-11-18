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

class ResultadosPesquisaUsuarioActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_resultados_pesquisa_usuario)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<ImageView>(R.id.lopesSetaVoltar32).setOnClickListener {
            startActivity(Intent(this, MenuPrincipalUsuarioActivity::class.java))
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

        // ===== RecyclerView (mesmo estilo do histórico) =====
        val rv = findViewById<RecyclerView>(R.id.rvResultados)
        rv.layoutManager = LinearLayoutManager(this)

        // Mock dos resultados - ajuste nomes dos drawables se necessário
        val resultados = listOf(
            HistoryBook(
                title = "O Quinze",
                author = "Rachel de Queiroz",
                coverRes = R.drawable.livro_rachelqueiroz, // ajuste se necessário
                availabilityText = "Disponível: físico",
                isAvailable = true,
                dateText = "Hoje"
            ),
            HistoryBook(
                title = "Análise de texto",
                author = "Rachel de Queiroz",
                coverRes = R.drawable.livro_tcc, // ajuste se necessário
                availabilityText = "Disponível: físico + digital",
                isAvailable = true,
                dateText = "Hoje"
            ),
            HistoryBook(
                title = "Quincas Borba",
                author = "Machado de Assis",
                coverRes = R.drawable.livro_quincas,
                availabilityText = "Indisponível",
                isAvailable = false,
                dateText = "Ontem"
            )
        )

        // Clique: se conter "O Quinze", abre o popup
        rv.adapter = HistoryAdapter(resultados) { livro ->
            if (livro.title.contains("O Quinze", ignoreCase = true)) {
                startActivity(Intent(this, PopupResultadosUsuarioActivity::class.java))
            }
        }
    }
}