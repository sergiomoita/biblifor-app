package com.example.biblifor

import com.example.biblifor.Book
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.biblifor.adapter.FavoritosPagedAdapter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.math.ceil
import kotlin.math.min

class FavoritosUsuarioActivity : AppCompatActivity() {

    private lateinit var rootLayout: ConstraintLayout

    private lateinit var rvFavoritos: RecyclerView
    private lateinit var btnPag1: TextView
    private lateinit var btnPag2: TextView
    private lateinit var btnPag3: TextView
    private lateinit var adapter: FavoritosPagedAdapter

    private lateinit var ivLupaFavoritos: ImageView
    private lateinit var containerSearchFavoritos: View
    private lateinit var etSearchFavoritos: EditText

    private val allFavoritos = mutableListOf<Book>()
    private val pageSize = 5
    private var currentPage = 1
    private var totalPages = 1

    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favoritos_usuario)

        db = Firebase.firestore

        rootLayout = findViewById(R.id.main)

        rvFavoritos = findViewById(R.id.rvFavoritos)
        btnPag1 = findViewById(R.id.btnPag1Favoritos)
        btnPag2 = findViewById(R.id.btnPag2Favoritos)
        btnPag3 = findViewById(R.id.btnPag3Favoritos)

        ivLupaFavoritos = findViewById(R.id.ivLupaFavoritos)
        containerSearchFavoritos = findViewById(R.id.containerSearchFavoritos)
        etSearchFavoritos = findViewById(R.id.etSearchFavoritos)

        // ✅ SETA DE VOLTAR (adição solicitada)
        findViewById<ImageView>(R.id.btnVoltarFavoritos).setOnClickListener {
            finish()
        }

        // ➤ BARRA INFERIOR COMPLETA RESTAURADA
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

        adapter = FavoritosPagedAdapter { }

        rvFavoritos.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvFavoritos.adapter = adapter
        rvFavoritos.setHasFixedSize(true)

        configurarBuscaAnimada()
        configurarFiltroTexto()

        btnPag1.setOnClickListener {
            if (currentPage > 1) irParaPagina(currentPage - 1)
        }
        btnPag3.setOnClickListener {
            if (currentPage < totalPages) irParaPagina(currentPage + 1)
        }

        carregarFavoritosDoUsuario()
    }

    // 🔥 BUSCAR FAVORITOS POR LIVRO-ID
    private fun carregarFavoritosDoUsuario() {
        val prefs = getSharedPreferences("APP_PREFS", MODE_PRIVATE)
        val matricula = prefs.getString("MATRICULA_USER", "") ?: ""

        if (matricula.isBlank()) return

        allFavoritos.clear()

        db.collection("alunos")
            .document(matricula)
            .collection("favoritos")
            .get()
            .addOnSuccessListener { favoritosDocs ->

                if (favoritosDocs.isEmpty) {
                    prepararPaginacao()
                    renderPage()
                    return@addOnSuccessListener
                }

                for (favDoc in favoritosDocs) {
                    val livroId = favDoc.id

                    db.collection("livros")
                        .document(livroId)
                        .get()
                        .addOnSuccessListener { livroDoc ->
                            if (!livroDoc.exists()) return@addOnSuccessListener

                            val tituloOriginal = livroDoc.getString("Titulo") ?: ""
                            val autor = livroDoc.getString("Autor") ?: ""
                            val situacao = livroDoc.getString("SituacaoEmprestimo") ?: ""
                            val disponibilidade = livroDoc.getString("Disponibilidade") ?: ""
                            val imagem = livroDoc.getString("Imagem")

                            val emprestavel = situacao.equals("Emprestável", true)

                            val livro = Book(
                                title = if (autor.isNotBlank()) "$tituloOriginal - $autor" else tituloOriginal,
                                coverRes = R.drawable.livro_1984,
                                emprestavel = emprestavel,
                                imagemBase64 = imagem,
                                tituloOriginal = tituloOriginal,
                                autor = autor,
                                situacaoEmprestimo = situacao,
                                disponibilidade = disponibilidade,
                                livroId = livroId
                            )

                            allFavoritos.add(livro)

                            allFavoritos.sortBy { it.title.lowercase() }
                            prepararPaginacao()
                            renderPage()
                        }
                }
            }
    }

    // -------- BUSCA --------
    private fun configurarFiltroTexto() {
        etSearchFavoritos.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                aplicarFiltroBusca(s.toString())
            }
        })
    }

    private fun aplicarFiltroBusca(texto: String) {
        val termo = texto.trim().lowercase()

        if (termo.isEmpty()) {
            allFavoritos.sortBy { it.title.lowercase() }
        } else {
            val combinam = allFavoritos.filter { it.title.lowercase().contains(termo) }
            val naoCombinam = allFavoritos.filter { !it.title.lowercase().contains(termo) }

            allFavoritos.clear()
            allFavoritos.addAll(combinam + naoCombinam)
        }

        currentPage = 1
        prepararPaginacao()
        renderPage()
    }

    private fun configurarBuscaAnimada() {
        ivLupaFavoritos.setOnClickListener {
            val mostrando = containerSearchFavoritos.visibility == View.VISIBLE

            TransitionManager.beginDelayedTransition(rootLayout, AutoTransition())

            if (mostrando) {
                containerSearchFavoritos.visibility = View.GONE
                esconderTeclado()
                etSearchFavoritos.clearFocus()
            } else {
                containerSearchFavoritos.visibility = View.VISIBLE
                etSearchFavoritos.requestFocus()
                mostrarTeclado()
            }
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN &&
            containerSearchFavoritos.visibility == View.VISIBLE
        ) {
            val rect = Rect()
            containerSearchFavoritos.getGlobalVisibleRect(rect)
            val x = ev.rawX.toInt()
            val y = ev.rawY.toInt()

            if (!rect.contains(x, y)) {
                TransitionManager.beginDelayedTransition(rootLayout, AutoTransition())
                containerSearchFavoritos.visibility = View.GONE
                esconderTeclado()
                etSearchFavoritos.clearFocus()
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun mostrarTeclado() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(etSearchFavoritos, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun esconderTeclado() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(etSearchFavoritos.windowToken, 0)
    }

    // -------- PAGINAÇÃO --------
    private fun prepararPaginacao() {
        val n = allFavoritos.size
        val needed = ceil(n / pageSize.toDouble()).toInt()
        totalPages = min(3, maxOf(needed, 1))
        currentPage = currentPage.coerceIn(1, totalPages)
    }

    private fun irParaPagina(p: Int) {
        if (p in 1..totalPages && p != currentPage) {
            currentPage = p
            renderPage()
        }
    }

    private fun renderPage() {
        val start = (currentPage - 1) * pageSize
        val end = min(start + pageSize, allFavoritos.size)

        val slice =
            if (start in 0 until end) allFavoritos.subList(start, end)
            else emptyList()

        adapter.submitPage(slice)
        rvFavoritos.scrollToPosition(0)
    }
}
