package com.example.biblifor

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class ResultadosPesquisaUsuarioActivity : BaseActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var rv: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_resultados_pesquisa_usuario)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // ======== BOTÕES ========
        findViewById<ImageView>(R.id.lopesSetaVoltar32).setOnClickListener {
            startActivity(Intent(this, MenuPrincipalUsuarioActivity::class.java))
        }
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

        // Firestore
        db = FirebaseFirestore.getInstance()

        // RecyclerView
        rv = findViewById(R.id.rvResultados)
        rv.layoutManager = LinearLayoutManager(this)

        // Título vindo da tela de pesquisa
        val tituloPesquisado = intent.getStringExtra("titulo_pesquisado") ?: ""

        buscarLivro(tituloPesquisado)
    }

    private fun buscarLivro(titulo: String) {
        db.collection("livros")
            .whereEqualTo("Titulo", titulo) //  ATENÇÃO → respeita maiúsculas
            .get()
            .addOnSuccessListener { result ->

                if (!result.isEmpty) {
                    val doc = result.documents[0]

                    // ========== PEGA OS CAMPOS DO FIREBASE ==========
                    val tituloLivro = doc.getString("Titulo") ?: "Sem título"
                    val autorLivro = doc.getString("Autor") ?: "Autor desconhecido"
                    val disponibilidade = doc.getString("Disponibilidade") ?: "Indefinido"

                    // Cover placeholder (até você colocar imagens reais)
                    val img = R.drawable.livro_padrao

                    val livro = HistoryBook(
                        title = tituloLivro,
                        author = autorLivro,
                        coverRes = img,
                        availabilityText = disponibilidade,
                        isAvailable = !disponibilidade.contains("Indisponível", true),
                        dateText = "Hoje",
                        statusText = null
                    )

                    // Mostra o item na lista
                    rv.adapter = HistoryAdapter(listOf(livro)) { item ->
                        // Abre popup com detalhes
                        startActivity(Intent(this, PopupResultadosUsuarioActivity::class.java))
                    }
                } else {
                    // Resultado vazio (não precisa tela separada)
                    rv.adapter = HistoryAdapter(emptyList()) { }
                }
            }
            .addOnFailureListener {
                // Erro → lista vazia
                rv.adapter = HistoryAdapter(emptyList()) { }
            }
    }
}
