package com.example.biblifor

import android.content.Context
import android.content.Intent       // 👈 IMPORT IMPORTANTE
import android.graphics.Rect
import android.graphics.Typeface
import android.os.Bundle
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
import kotlin.math.ceil
import kotlin.math.min

class FavoritosUsuarioActivity : AppCompatActivity() {

    private lateinit var rootLayout: ConstraintLayout

    private lateinit var rvFavoritos: RecyclerView
    private lateinit var btnPag1: TextView   // seta "<"
    private lateinit var btnPag2: TextView   // número da página
    private lateinit var btnPag3: TextView   // seta ">"
    private lateinit var adapter: FavoritosPagedAdapter

    // Busca
    private lateinit var ivLupaFavoritos: ImageView
    private lateinit var containerSearchFavoritos: View
    private lateinit var etSearchFavoritos: EditText

    private val allFavoritos = mutableListOf<Book>()
    private val pageSize = 5
    private var currentPage = 1
    private var totalPages = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favoritos_usuario)

        // Root para animação
        rootLayout = findViewById(R.id.main)

        rvFavoritos = findViewById(R.id.rvFavoritos)
        btnPag1 = findViewById(R.id.btnPag1Favoritos)
        btnPag2 = findViewById(R.id.btnPag2Favoritos)
        btnPag3 = findViewById(R.id.btnPag3Favoritos)

        ivLupaFavoritos = findViewById(R.id.ivLupaFavoritos)
        containerSearchFavoritos = findViewById(R.id.containerSearchFavoritos)
        etSearchFavoritos = findViewById(R.id.etSearchFavoritos)

        // ===== Navegação inferior =====
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
        // ===============================

        adapter = FavoritosPagedAdapter { book ->
            // TODO: ação ao clicar no livro favorito
        }

        rvFavoritos.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvFavoritos.adapter = adapter
        rvFavoritos.setHasFixedSize(true)

        allFavoritos.addAll(gerarMockComSeusDrawables())

        prepararPaginacao()
        renderPage()
        aplicarEstiloBotoes()
        configurarBuscaAnimada()

        // Botão esquerdo: página anterior
        btnPag1.setOnClickListener {
            if (currentPage > 1) {
                irParaPagina(currentPage - 1)
            }
        }

        // Botão direito: próxima página
        btnPag3.setOnClickListener {
            if (currentPage < totalPages) {
                irParaPagina(currentPage + 1)
            }
        }
    }

    private fun configurarBuscaAnimada() {
        ivLupaFavoritos.setOnClickListener {
            val mostrando = containerSearchFavoritos.visibility == View.VISIBLE

            // Anima o layout quando a visibilidade muda
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

    // Fecha a barra se tocar fora dela
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN &&
            this::containerSearchFavoritos.isInitialized &&
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
            aplicarEstiloBotoes()
        }
    }

    private fun renderPage() {
        val start = (currentPage - 1) * pageSize
        val end = min(start + pageSize, allFavoritos.size)
        val slice = if (start in 0 until end) allFavoritos.subList(start, end) else emptyList()
        adapter.submitPage(slice)
        rvFavoritos.scrollToPosition(0)
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
            setTextColor(
                ContextCompat.getColor(
                    this@FavoritosUsuarioActivity,
                    android.R.color.black
                )
            )
            background = ContextCompat.getDrawable(
                this@FavoritosUsuarioActivity,
                R.drawable.bg_page_button_white
            )
        }

        // Esquerda: seta "<"
        btnPag1.config(
            texto = "<",
            habilitado = currentPage > 1,
            isCurrent = false
        )

        // Meio: número da página atual
        btnPag2.config(
            texto = currentPage.toString(),
            habilitado = true,
            isCurrent = true
        )

        // Direita: seta ">"
        btnPag3.config(
            texto = ">",
            habilitado = currentPage < totalPages,
            isCurrent = false
        )
    }

    private fun gerarMockComSeusDrawables(): List<Book> {
        val capas = listOf(
            R.drawable.livro_1984,
            R.drawable.livro_antigona,
            R.drawable.livro_banana,
            R.drawable.livro_cleancode,
            R.drawable.livro_crime_e_castigo,
            R.drawable.livro_dom_casmurro,
            R.drawable.livro_dos_juizes,
            R.drawable.livro_edipo_rei,
            R.drawable.livro_guerra_e_paz,
            R.drawable.livro_iliada,
            R.drawable.livro_metamorfose,
            R.drawable.livro_napoleao,
            R.drawable.livro_quincas,
            R.drawable.livro_rachelqueiroz,
            R.drawable.livro_rev_bichos,
            R.drawable.livro_romeu1,
            R.drawable.livro_romeu2,
            R.drawable.livro_socrates,
            R.drawable.livro_tcc,
            R.drawable.livro_texto_academico,
            R.drawable.livro_turmamonica
        )
        return capas.mapIndexed { i, res ->
            Book(
                title = "Livro ${i + 1}",
                coverRes = res,
                emprestavel = (i % 2 == 0)
            )
        }
    }
}
