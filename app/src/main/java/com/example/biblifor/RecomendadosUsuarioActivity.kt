package com.example.biblifor

import android.content.Context
import android.content.Intent
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
import androidx.activity.enableEdgeToEdge
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.ceil
import kotlin.math.min

class RecomendadosUsuarioActivity : BaseActivity() {

    private lateinit var rootLayout: ConstraintLayout

    private lateinit var rvRecomendados: RecyclerView
    private lateinit var btnPagEsq: TextView   // "<"
    private lateinit var btnPagCentro: TextView // número da página
    private lateinit var btnPagDir: TextView   // ">"
    private lateinit var adapter: FavoritosPagedAdapter

    // Busca
    private lateinit var ivLupaRecomendados: ImageView
    private lateinit var containerSearchRecomendados: View
    private lateinit var etSearchRecomendados: EditText

    // Dados + paginação
    private val allRecomendados = mutableListOf<Book>()
    private val pageSize = 5           // mesmo padrão dos Favoritos
    private var currentPage = 1
    private var totalPages = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_recomendados_usuario)

        // Insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Root para animação
        rootLayout = findViewById(R.id.main)

        rvRecomendados = findViewById(R.id.rvRecomendados)
        btnPagEsq = findViewById(R.id.btnPag1Recomendados)
        btnPagCentro = findViewById(R.id.btnPag2Recomendados)
        btnPagDir = findViewById(R.id.btnPag3Recomendados)

        ivLupaRecomendados = findViewById(R.id.ivLupaRecomendados)
        containerSearchRecomendados = findViewById(R.id.containerSearchRecomendados)
        etSearchRecomendados = findViewById(R.id.etSearchRecomendados)

        // ===== 1 livro por linha, mesmo layout dos Favoritos =====
        rvRecomendados.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvRecomendados.setHasFixedSize(true)

        // Usa o MESMO adapter de Favoritos (mesmo card)
        adapter = FavoritosPagedAdapter { book ->
            // Se quiser manter ações especiais, pode usar o título aqui
            if (book.title.contains("Guerra e Paz", ignoreCase = true)) {
                startActivity(Intent(this, PopupEmprestimoProibidoUsuarioActivity::class.java))
            } else if (book.title.contains("Romeu", ignoreCase = true)) {
                startActivity(Intent(this, PopupLivroOnlineUsuarioActivity::class.java))
            }
        }
        rvRecomendados.adapter = adapter

        // Botão voltar
        findViewById<ImageView>(R.id.btnVoltarRecomendados).setOnClickListener {
            finish()
        }

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

        // ===== MESMOS LIVROS DOS FAVORITOS =====
        allRecomendados.addAll(gerarMockComSeusDrawablesRecomendados())

        prepararPaginacao()
        renderPage()
        aplicarEstiloBotoes()
        configurarBuscaAnimada()

        // Paginação: < e >
        btnPagEsq.setOnClickListener {
            if (currentPage > 1) {
                irParaPagina(currentPage - 1)
            }
        }

        btnPagDir.setOnClickListener {
            if (currentPage < totalPages) {
                irParaPagina(currentPage + 1)
            }
        }
    }

    // ===== BUSCA ANIMADA =====
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
            this::containerSearchRecomendados.isInitialized &&
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

    // ===== PAGINAÇÃO =====
    private fun prepararPaginacao() {
        val n = allRecomendados.size
        val needed = ceil(n / pageSize.toDouble()).toInt()
        totalPages = min(3, maxOf(needed, 1))   // até 3 páginas, igual Favoritos
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
            setTextColor(
                ContextCompat.getColor(
                    this@RecomendadosUsuarioActivity,
                    android.R.color.black
                )
            )
            background = ContextCompat.getDrawable(
                this@RecomendadosUsuarioActivity,
                R.drawable.bg_page_button_white
            )
        }

        btnPagEsq.config("<", currentPage > 1, false)
        btnPagCentro.config(currentPage.toString(), true, true)
        btnPagDir.config(">", currentPage < totalPages, false)
    }

    // ===== MOCK IGUAL AO DOS FAVORITOS =====
    private fun gerarMockComSeusDrawablesRecomendados(): List<Book> {
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
