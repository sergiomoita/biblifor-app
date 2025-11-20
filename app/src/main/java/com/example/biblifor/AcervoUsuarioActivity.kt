package com.example.biblifor

import Book
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
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.math.ceil
import kotlin.math.min

class AcervoUsuarioActivity : BaseActivity() {

    private lateinit var rootLayout: ConstraintLayout

    private lateinit var rvAcervo: RecyclerView
    private lateinit var btnPag1: TextView
    private lateinit var btnPag2: TextView
    private lateinit var btnPag3: TextView
    private lateinit var adapter: FavoritosPagedAdapter

    // Busca
    private lateinit var ivLupaAcervo: ImageView
    private lateinit var containerSearchAcervo: View
    private lateinit var etSearchAcervo: EditText

    private val allBooks = mutableListOf<Book>()
    private val pageSize = 5
    private var currentPage = 1
    private var totalPages = 1

    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_acervo_usuario)

        db = Firebase.firestore

        rootLayout = findViewById(R.id.main)

        rvAcervo = findViewById(R.id.rvAcervo)
        btnPag1 = findViewById(R.id.btnPag1Acervo)
        btnPag2 = findViewById(R.id.btnPag2Acervo)
        btnPag3 = findViewById(R.id.btnPag3Acervo)

        ivLupaAcervo = findViewById(R.id.ivLupaAcervo)
        containerSearchAcervo = findViewById(R.id.containerSearchAcervo)
        etSearchAcervo = findViewById(R.id.etSearchAcervo)

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

        findViewById<ImageView>(R.id.btnVoltarAcervo).setOnClickListener {
            finish()
        }

        adapter = FavoritosPagedAdapter { /* clique no livro */ }

        rvAcervo.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvAcervo.adapter = adapter
        rvAcervo.setHasFixedSize(true)

        configurarBuscaAnimada()
        configurarFiltroTexto()

        btnPag1.setOnClickListener {
            if (currentPage > 1) irParaPagina(currentPage - 1)
        }
        btnPag3.setOnClickListener {
            if (currentPage < totalPages) irParaPagina(currentPage + 1)
        }

        carregarLivrosDoFirebase()
    }

    private fun carregarLivrosDoFirebase() {
        db.collection("livros")
            .get()
            .addOnSuccessListener { result ->
                allBooks.clear()

                for (doc in result) {
                    val titulo = doc.getString("Titulo") ?: continue
                    val autor = doc.getString("Autor") ?: ""
                    val situacaoEmprestimo = doc.getString("SituacaoEmprestimo") ?: ""
                    val imagemBase64 = doc.getString("Imagem")

                    val emprestavel =
                        situacaoEmprestimo.equals("Emprestável", ignoreCase = true)

                    val tituloComAutor =
                        if (autor.isNotBlank()) "$titulo - $autor" else titulo

                    allBooks.add(
                        Book(
                            title = tituloComAutor,
                            coverRes = R.drawable.livro_1984,
                            emprestavel = emprestavel,
                            imagemBase64 = imagemBase64
                        )
                    )
                }

                allBooks.sortBy { it.title.lowercase() }

                prepararPaginacao()
                renderPage()
                aplicarEstiloBotoes()
            }
    }

    // 🔥 FILTRO IGUAL AO DA TELA DE EMPRESTÁVEIS DO ADM
    private fun configurarFiltroTexto() {
        etSearchAcervo.addTextChangedListener(object : TextWatcher {
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
            allBooks.sortBy { it.title.lowercase() }
        } else {
            val combinam = allBooks.filter { it.title.lowercase().contains(termo) }
            val naoCombinam = allBooks.filter { !it.title.lowercase().contains(termo) }

            allBooks.clear()
            allBooks.addAll(combinam + naoCombinam)
        }

        currentPage = 1
        prepararPaginacao()
        renderPage()
        aplicarEstiloBotoes()
    }

    private fun configurarBuscaAnimada() {
        ivLupaAcervo.setOnClickListener {
            val mostrando = containerSearchAcervo.visibility == View.VISIBLE

            TransitionManager.beginDelayedTransition(rootLayout, AutoTransition())

            if (mostrando) {
                containerSearchAcervo.visibility = View.GONE
                esconderTeclado()
                etSearchAcervo.clearFocus()
            } else {
                containerSearchAcervo.visibility = View.VISIBLE
                etSearchAcervo.requestFocus()
                mostrarTeclado()
            }
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN &&
            containerSearchAcervo.visibility == View.VISIBLE
        ) {
            val rect = Rect()
            containerSearchAcervo.getGlobalVisibleRect(rect)
            val x = ev.rawX.toInt()
            val y = ev.rawY.toInt()

            if (!rect.contains(x, y)) {
                TransitionManager.beginDelayedTransition(rootLayout, AutoTransition())
                containerSearchAcervo.visibility = View.GONE
                esconderTeclado()
                etSearchAcervo.clearFocus()
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun mostrarTeclado() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(etSearchAcervo, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun esconderTeclado() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(etSearchAcervo.windowToken, 0)
    }

    private fun prepararPaginacao() {
        val n = allBooks.size
        val needed = ceil(n / pageSize.toDouble()).toInt()
        totalPages = min(3, maxOf(needed, 1))
        currentPage = currentPage.coerceIn(1, totalPages)
    }

    private fun irParaPagina(p: Int) {
        if (p in 1..totalPages && p != currentPage) {
            currentPage = p
            renderPage()
            aplicarEstiloBotoes()
        }
    }

    private fun renderPage() {
        val start = (currentPage - 1) * pageSize
        val end = min(start + pageSize, allBooks.size)
        val slice = if (start in 0 until end) allBooks.subList(start, end) else emptyList()

        adapter.submitPage(slice)
        rvAcervo.scrollToPosition(0)
    }

    private fun aplicarEstiloBotoes() {
        fun TextView.config(texto: String, habilitado: Boolean, isCurrent: Boolean) {
            text = texto
            isEnabled = habilitado
            typeface = if (isCurrent) Typeface.DEFAULT_BOLD else Typeface.DEFAULT
            alpha = when {
                !habilitado -> 0.35f
                isCurrent -> 1f
                else -> 0.95f
            }
            setTextColor(ContextCompat.getColor(this@AcervoUsuarioActivity, android.R.color.black))
            background = ContextCompat.getDrawable(
                this@AcervoUsuarioActivity,
                R.drawable.bg_page_button_white
            )
        }

        btnPag1.config("<", currentPage > 1, false)
        btnPag2.config(currentPage.toString(), true, true)
        btnPag3.config(">", currentPage < totalPages, false)
    }
}
