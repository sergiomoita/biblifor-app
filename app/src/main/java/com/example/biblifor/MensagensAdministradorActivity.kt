package com.example.biblifor

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.biblifor.adapter.AvisoAdapter
import com.example.biblifor.model.Aviso

class MensagensAdministradorActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_mensagens_administrador)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // === RecyclerView de eventos (reaproveitando AvisoAdapter/Aviso) ===
        val rv = findViewById<RecyclerView>(R.id.rvEventosAdmin)
        rv.layoutManager = LinearLayoutManager(this)

        val eventos = listOf(
            Aviso(
                "Hugo Moura – Biblioteca – Alunos",
                "12/09/2025",
                "Olá, Alunos. Este é um convite para participar da Tarde de Leitura e Café na biblioteca, no dia 18/09/2025 às 15h. Traga seu livro favorito e compartilhe a experiência com outros leitores."
            ),
            Aviso(
                "Leonilha Lessa – Biblioteca – Geral",
                "25/08/2025",
                "Participe da Oficina de Escrita Criativa na biblioteca no dia 25/09/2025 às 14h. Uma ótima oportunidade para soltar a imaginação e criar novos textos."
            ),
            Aviso(
                "Hugo Moura – Biblioteca – Alunos",
                "12/09/2025",
                "Olá, Alunos. Este é um convite para participar da Tarde de Leitura e Café na biblioteca, no dia 18/09/2025 às 15h. Traga seu livro favorito e compartilhe a experiência com outros leitores."
            ),
            Aviso(
                "Leonilha Lessa – Biblioteca – Geral",
                "25/08/2025",
                "Participe da Oficina de Escrita Criativa na biblioteca no dia 25/09/2025 às 14h. Uma ótima oportunidade para soltar a imaginação e criar novos textos."
            ),
            Aviso(
                "Hugo Moura – Biblioteca – Alunos",
                "12/09/2025",
                "Olá, Alunos. Este é um convite para participar da Tarde de Leitura e Café na biblioteca, no dia 18/09/2025 às 15h. Traga seu livro favorito e compartilhe a experiência com outros leitores."
            ),
            Aviso(
                "Leonilha Lessa – Biblioteca – Geral",
                "25/08/2025",
                "Participe da Oficina de Escrita Criativa na biblioteca no dia 25/09/2025 às 14h. Uma ótima oportunidade para soltar a imaginação e criar novos textos."
            ),
            Aviso(
                "Hugo Moura – Biblioteca – Alunos",
                "12/09/2025",
                "Olá, Alunos. Este é um convite para participar da Tarde de Leitura e Café na biblioteca, no dia 18/09/2025 às 15h. Traga seu livro favorito e compartilhe a experiência com outros leitores."
            ),
            Aviso(
                "Leonilha Lessa – Biblioteca – Geral",
                "25/08/2025",
                "Participe da Oficina de Escrita Criativa na biblioteca no dia 25/09/2025 às 14h. Uma ótima oportunidade para soltar a imaginação e criar novos textos."
            )
        )

        rv.adapter = AvisoAdapter(eventos)

        // === Ações de UI ===
        findViewById<ImageView>(R.id.iconNovaMensagem).setOnClickListener {
            startActivity(Intent(this, EscreverMensagemAdministradorActivity::class.java)); finish()
        }
        findViewById<TextView>(R.id.textNovaMensagem).setOnClickListener {
            startActivity(Intent(this, EscreverMensagemAdministradorActivity::class.java)); finish()
        }

        // ⚙️ Barra inferior
        findViewById<ImageView>(R.id.iconHomeCapsulasAdmSergio).setOnClickListener {
            startActivity(Intent(this, MenuPrincipalAdministradorActivity::class.java)); finish()
        }
        findViewById<ImageView>(R.id.iconEscreverMsgCapsulasAdmSergio).setOnClickListener {
            startActivity(Intent(this, EscreverMensagemAdministradorActivity::class.java))
        }
        findViewById<ImageView>(R.id.iconMensagemCapsulasAdmSergio).setOnClickListener {
            startActivity(Intent(this, MensagensAdministradorActivity::class.java))
        }
        findViewById<ImageView>(R.id.iconMenuInferiorCapsulasAdmSergio).setOnClickListener {
            startActivity(Intent(this, MenuHamburguerAdministradorActivity::class.java)); finish()
        }
    }
}
