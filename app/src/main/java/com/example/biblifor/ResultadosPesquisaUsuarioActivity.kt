package com.example.biblifor

import android.content.Intent
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ResultadosPesquisaUsuarioActivity : AppCompatActivity() {

    private lateinit var root: ConstraintLayout
    private lateinit var rv: RecyclerView
    private lateinit var etPesquisa: EditText
    private lateinit var containerPesquisa: androidx.cardview.widget.CardView
    private lateinit var lupaSuperior: ImageView
    private lateinit var lupaInterna: ImageView

    private lateinit var adapter: FavoritosPagedAdapter
    private val resultados = mutableListOf<Book>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_resultados_pesquisa_usuario)

        // ====== Referências ======
        root = findViewById(R.id.main)
        rv = findViewById(R.id.rvResultados)
        etPesquisa = findViewById(R.id.editPesquisaResultados)
        containerPesquisa = findViewById(R.id.containerPesquisaResultados)
        lupaSuperior = findViewById(R.id.ivLupaResultados)
        lupaInterna = findViewById(R.id.ivLupaInternaResultados)

        // ====== Adapter ======
        rv.layoutManager = LinearLayoutManager(this)
        adapter = FavoritosPagedAdapter { livro ->
            if (livro.title.contains("Romeu", ignoreCase = true)) {
                startActivity(Intent(this, PopupResultadosUsuarioActivity::class.java))
            }
        }
        rv.adapter = adapter

        // ====== SETA VOLTAR ======
        findViewById<ImageView>(R.id.lopesSetaVoltar32).setOnClickListener {
            startActivity(Intent(this, MenuPrincipalUsuarioActivity::class.java))
            finish()
        }

        // ====== Recebe termo inicial ======
        val termo = intent.getStringExtra("pesquisa") ?: ""
        etPesquisa.setText(termo)

        // ====== Pesquisa inicial ======
        realizarPesquisa(termo)

        // ====== Lupa superior (abre/fecha a barra) ======
        lupaSuperior.setOnClickListener {
            val visible = containerPesquisa.visibility == android.view.View.VISIBLE

            TransitionManager.beginDelayedTransition(root, AutoTransition())
            containerPesquisa.visibility =
                if (visible) android.view.View.GONE else android.view.View.VISIBLE

            if (!visible) {
                // abrir → focar no campo
                etPesquisa.requestFocus()
            }
        }

        // ====== Lupa interna (realiza a pesquisa) ======
        lupaInterna.setOnClickListener {
            val texto = etPesquisa.text.toString().trim()
            if (texto.isNotEmpty()) {
                realizarPesquisa(texto)
            }
        }

        // ====== Navegação inferior ======
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
    }

    // ======================================================
    // 🔥 REALIZAR PESQUISA — SÓ DISPARA AQUI
    // ======================================================
    private fun realizarPesquisa(texto: String) {
        val db = Firebase.firestore
        resultados.clear()

        db.collection("livros")
            .get()
            .addOnSuccessListener { result ->
                val pesquisaLower = texto.lowercase()

                for (doc in result) {
                    val titulo = doc.getString("Titulo") ?: continue
                    val autor = doc.getString("Autor") ?: ""
                    val imagem = doc.getString("Imagem")
                    val situacao = doc.getString("SituacaoEmprestimo") ?: ""

                    val combinado = "$titulo $autor".lowercase()

                    if (!combinado.contains(pesquisaLower)) continue

                    resultados.add(
                        Book(
                            title = "$titulo - $autor",
                            coverRes = R.drawable.livro_socrates,
                            emprestavel = situacao.equals("Emprestável", true),
                            imagemBase64 = imagem
                        )
                    )
                }

                // Se nada encontrado → vai para a tela sem resultado
                if (resultados.isEmpty()) {
                    val intent = Intent(this, MensagemSemResultadoUsuarioActivity::class.java)
                    intent.putExtra("pesquisa", texto)
                    startActivity(intent)
                    finish()
                } else {
                    adapter.submitPage(resultados.toList())
                }
            }
    }
}
