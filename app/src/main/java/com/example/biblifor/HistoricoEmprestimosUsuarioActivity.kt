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

class HistoricoEmprestimosUsuarioActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_historico_emprestimos_usuario)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // --- Navegação inferior ---
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
        val rv = findViewById<RecyclerView>(R.id.rvHistorico)
        rv.layoutManager = LinearLayoutManager(this)

        val historico = listOf(
            HistoryBook(
                title = "Romeu e Julieta",
                author = "W. Shakespeare",
                coverRes = R.drawable.livro_romeu2,
                availabilityText = "Disponível: físico + digital",
                isAvailable = true,
                dateText = "13/09/2025"
            ),
            HistoryBook(
                title = "1984",
                author = "George Orwell",
                coverRes = R.drawable.livro_1984,
                availabilityText = "Disponível: físico",
                isAvailable = true,
                dateText = "02/09/2025",
                statusText = "Empréstimo em finalizado"
            ),
            HistoryBook(
                title = "Dom Casmurro",
                author = "Machado de Assis",
                coverRes = R.drawable.livro_dom_casmurro,
                availabilityText = "Indisponível",
                isAvailable = false,
                dateText = "28/08/2025",
                statusText = "Empréstimo em finalizado"
            ),
            HistoryBook(
                title = "Código Limpo",
                author = "Robert C. Martin",
                coverRes = R.drawable.livro_cleancode,
                availabilityText = "Disponível: físico + digital",
                isAvailable = true,
                dateText = "10/08/2025"
            ),
            HistoryBook(
                title = "Antígona",
                author = "Sófocles",
                coverRes = R.drawable.livro_antigona,
                availabilityText = "Disponível: digital",
                isAvailable = true,
                dateText = "24/07/2025"
            ),
            HistoryBook(
                title = "Guerra e Paz",
                author = "L. Tolstói",
                coverRes = R.drawable.livro_guerra_e_paz,
                availabilityText = "Disponível: físico + digital",
                isAvailable = true,
                dateText = "15/07/2025"
            )
        )

        // --- Adapter com clique ---
        rv.adapter = HistoryAdapter(historico) { livro ->
            if (livro.title == "Romeu e Julieta") {
                val intent = Intent(this, PopupHistoricoEmprestimosUsuarioActivity::class.java)
                startActivity(intent)
            }
        }
    }
}
