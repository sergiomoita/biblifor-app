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
import androidx.activity.enableEdgeToEdge
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.math.ceil
import kotlin.math.min

class RecomendadosUsuarioActivity : BaseActivity() {

    private lateinit var rootLayout: ConstraintLayout

    private lateinit var rvRecomendados: RecyclerView
    private lateinit var btnPagEsq: TextView
    private lateinit var btnPagCentro: TextView
    private lateinit var btnPagDir: TextView
    private lateinit var adapter: FavoritosPagedAdapter

    // Busca
    private lateinit var ivLupaRecomendados: ImageView
    private lateinit var containerSearchRecomendados: View
    private lateinit var etSearchRecomendados: EditText

    private val allRecomendados = mutableListOf<Book>()
    private val pageSize = 5
    private var currentPage = 1
    private var totalPages = 1

    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_recomendados_usuario)

        db = Firebase.firestore

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        rootLayout = findViewById(R.id.main)

        rvRecomendados = findViewById(R.id.rvRecomendados)
        btnPagEsq = findViewById(R.id.btnPag1Recomendados)
        btnPagCentro = findViewById(R.id.btnPag2Recomendados)
        btnPagDir = findViewById(R.id.btnPag3Recomendados)

        ivLupaRecomendados = findViewById(R.id.ivLupaRecomendados)
        containerSearchRecomendados = findViewById(R.id.containerSearchRecomendados)
        etSearchRecomendados = findViewById(R.id.etSearchRecomendados)

        rvRecomendados.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvRecomendados.setHasFixedSize(true)

        adapter = FavoritosPagedAdapter { book ->
            if (book.title.contains("Guerra e Paz", ignoreCase = true)) {
                startActivity(Intent(this, PopupEmprestimoProibidoUsuarioActivity::class.java))
            } else if (book.title.contains("Romeu", ignoreCase = true)) {
                startActivity(Intent(this, PopupLivroOnlineUsuarioActivity::class.java))
            }
        }

        rvRecomendados.adapter = adapter

        // Botão voltar
        findViewById<ImageView>(R.id.btnVoltarRecomendados).setOnClickListener { finish() }

        // Barra inferior
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

        configurarBuscaAnimada()
        configurarFiltroTexto()

        btnPagEsq.setOnClickListener {
            if (currentPage > 1) irParaPagina(currentPage - 1)
        }
        btnPagDir.setOnClickListener {
            if (currentPage < totalPages) irParaPagina(currentPage + 1)
        }

        carregarLivrosDoFirebase()
    }

    private fun carregarLivrosDoFirebase() {
        db.collection("livros")
            .whereEqualTo("recomendar", true)
            .get()
            .addOnSuccessListener { result ->
                allRecomendados.clear()

                for (doc in result) {

                    val tituloOriginal = doc.getString("Titulo") ?: continue
                    val autor = doc.getString("Autor") ?: ""
                    val situacaoEmprestimo = doc.getString("SituacaoEmprestimo") ?: ""
                    val disponibilidade = doc.getString("Disponibilidade") ?: ""
                    val imagemBase64 = doc.getString("Imagem")

                    val emprestavel = situacaoEmprestimo.equals("Emprestável", true)

                    val tituloExibicao =
                        if (autor.isNotBlank()) "$tituloOriginal - $autor"
                        else tituloOriginal

                    val livro = Book(
                        title = tituloExibicao,
                        coverRes = R.drawable.livro_1984,   // fallback
                        emprestavel = emprestavel,
                        imagemBase64 = imagemBase64,

                        tituloOriginal = tituloOriginal,
                        autor = autor,
                        situacaoEmprestimo = situacaoEmprestimo,
                        disponibilidade = disponibilidade
                    )

                    allRecomendados.add(livro)
                }

                allRecomendados.sortBy { it.title.lowercase() }

                prepararPaginacao()
                renderPage()
                aplicarEstiloBotoes()
            }
    }



    // 🔥 FILTRO — IGUAL AO DAS OUTRAS TELAS
    private fun configurarFiltroTexto() {
        etSearchRecomendados.addTextChangedListener(object : TextWatcher {
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
            allRecomendados.sortBy { it.title.lowercase() }
        } else {
            val combinam = allRecomendados.filter { it.title.lowercase().contains(termo) }
            val naoCombinam = allRecomendados.filter { !it.title.lowercase().contains(termo) }

            allRecomendados.clear()
            allRecomendados.addAll(combinam + naoCombinam)
        }

        currentPage = 1
        prepararPaginacao()
        renderPage()
        aplicarEstiloBotoes()
    }

    private fun configurarBuscaAnimada() {
        ivLupaRecomendados.setOnClickListener {
            val mostrando = containerSearchRecomendados.visibility == View.VISIBLE

            TransitionManager.beginDelayedTransition(rootLayout, AutoTransition())

            if (mostrando) {
                containerSearchRecomendados.visibility = View.GONE
                esconderTeclado()
                etSearchRecomendados.clearFocus()
            } else {
                containerSearchRecomendados.visibility = View.VISIBLE
                etSearchRecomendados.requestFocus()
                mostrarTeclado()
            }
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN &&
            containerSearchRecomendados.visibility == View.VISIBLE
        ) {
            val rect = Rect()
            containerSearchRecomendados.getGlobalVisibleRect(rect)
            val x = ev.rawX.toInt()
            val y = ev.rawY.toInt()

            if (!rect.contains(x, y)) {
                TransitionManager.beginDelayedTransition(rootLayout, AutoTransition())
                containerSearchRecomendados.visibility = View.GONE
                esconderTeclado()
                etSearchRecomendados.clearFocus()
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun mostrarTeclado() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(etSearchRecomendados, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun esconderTeclado() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(etSearchRecomendados.windowToken, 0)
    }

    private fun prepararPaginacao() {
        val n = allRecomendados.size
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
        val end = min(start + pageSize, allRecomendados.size)

        val slice =
            if (start in 0 until end) allRecomendados.subList(start, end) else emptyList()

        adapter.submitPage(slice)
        rvRecomendados.scrollToPosition(0)
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
            setTextColor(ContextCompat.getColor(this@RecomendadosUsuarioActivity, android.R.color.black))
            background = ContextCompat.getDrawable(
                this@RecomendadosUsuarioActivity,
                R.drawable.bg_page_button_white
            )
        }

        btnPagEsq.config("<", currentPage > 1, false)
        btnPagCentro.config(currentPage.toString(), true, true)
        btnPagDir.config(">", currentPage < totalPages, false)
    }
}
