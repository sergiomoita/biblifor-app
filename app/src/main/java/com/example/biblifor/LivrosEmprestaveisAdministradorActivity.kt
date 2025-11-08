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

class LivrosEmprestaveisAdministradorActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_livros_emprestaveis_administrador)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Voltar
        val leoBotaoVoltarPU5 = findViewById<ImageView>(R.id.leoImagemSetaPU5)
        leoBotaoVoltarPU5.setOnClickListener {
            startActivity(Intent(this, MenuPrincipalAdministradorActivity::class.java))
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
            startActivity(Intent(this, MenuPrincipalAdministradorActivity::class.java)); finish()
        }

        // ====== RecyclerView (grid) ======
        val rv = findViewById<RecyclerView>(R.id.rvLivros)
        rv.layoutManager = GridLayoutManager(this, 2)

        val livros = listOf(
            Book("Édipo Rei - Sófocles", R.drawable.livro_edipo_rei, true),
            Book("Diário de um Banana – 1", R.drawable.livro_banana, false),
            Book("Crime e Castigo - F. Dostoiévski", R.drawable.livro_crime_e_castigo, false),
            Book("Dom Casmurro - M. de Assis", R.drawable.livro_dom_casmurro, true),
            Book("A Apologia de Sócrates", R.drawable.livro_socrates, true),
            Book("Ilíada - Homero", R.drawable.livro_iliada, false)
        )

        // Abre EmprestarLivroAdministradorActivity quando clicar em "Crime e Castigo"
        rv.adapter = BookAdapter(livros) { livro ->
            if (livro.title.contains("Crime e Castigo", ignoreCase = true)) {
                startActivity(Intent(this, EmprestarLivroAdministradorActivity::class.java))
            }
        }
    }
}
