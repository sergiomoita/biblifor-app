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
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.ceil
import kotlin.math.min

class HistoricoEmprestimosUsuarioActivity : AppCompatActivity() {

    private lateinit var rootLayout: ConstraintLayout

    private lateinit var rvHistorico: RecyclerView
    private lateinit var btnPagPrev: TextView   // "<"
    private lateinit var btnPagCenter: TextView // número da página
    private lateinit var btnPagNext: TextView   // ">"

    private lateinit var adapter: HistoryPagedAdapter

    // Busca
    private lateinit var ivLupaHistorico: ImageView
    private lateinit var containerSearchHistorico: View
    private lateinit var etSearchHistorico: EditText

    private val allHistorico = mutableListOf<HistoryBook>()
    private val pageSize = 5
    private var currentPage = 1
    private var totalPages = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historico_emprestimos_usuario)

        rootLayout = findViewById(R.id.main)

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
            startActivity(Intent(this, MenuHamburguerUsuarioActivity::class.java))
        }

        // Voltar
        findViewById<ImageView>(R.id.btnVoltarHistorico).setOnClickListener {
            finish()
        }

        rvHistorico = findViewById(R.id.rvHistorico)
        btnPagPrev = findViewById(R.id.btnPagPrevHistorico)
        btnPagCenter = findViewById(R.id.btnPagCenterHistorico)
        btnPagNext = findViewById(R.id.btnPagNextHistorico)

        ivLupaHistorico = findViewById(R.id.ivLupaHistorico)
        containerSearchHistorico = findViewById(R.id.containerSearchHistorico)
        etSearchHistorico = findViewById(R.id.etSearchHistorico)

        adapter = HistoryPagedAdapter { livro ->
            // Clique no livro do histórico
            if (livro.title.contains("Romeu e Julieta", ignoreCase = true)) {
                val intent = Intent(this, PopupHistoricoEmprestimosUsuarioActivity::class.java)
                startActivity(intent)
            }
        }

        rvHistorico.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvHistorico.adapter = adapter
        rvHistorico.setHasFixedSize(true)

        // ===== TODOS OS LIVROS (mesmos de favoritos/recomendados) =====
        allHistorico.addAll(
            listOf(
                HistoryBook(
                    title = "Romeu e Julieta",
                    author = "W. Shakespeare",
                    coverRes = R.drawable.livro_romeu2,
                    availabilityText = "Disponível: físico + digital",
                    isAvailable = true,
                    dateText = "13/09/2025",
                    statusText = "Empréstimo finalizado"
                ),
                HistoryBook(
                    title = "1984",
                    author = "George Orwell",
                    coverRes = R.drawable.livro_1984,
                    availabilityText = "Disponível: físico",
                    isAvailable = true,
                    dateText = "02/09/2025",
                    statusText = "Empréstimo finalizado"
                ),
                HistoryBook(
                    title = "Dom Casmurro",
                    author = "Machado de Assis",
                    coverRes = R.drawable.livro_dom_casmurro,
                    availabilityText = "Indisponível",
                    isAvailable = false,
                    dateText = "28/08/2025",
                    statusText = "Empréstimo finalizado"
                ),
                HistoryBook(
                    title = "Código Limpo",
                    author = "Robert C. Martin",
                    coverRes = R.drawable.livro_cleancode,
                    availabilityText = "Disponível: físico + digital",
                    isAvailable = true,
                    dateText = "10/08/2025",
                    statusText = "Empréstimo finalizado"
                ),
                HistoryBook(
                    title = "Antígona",
                    author = "Sófocles",
                    coverRes = R.drawable.livro_antigona,
                    availabilityText = "Disponível: digital",
                    isAvailable = true,
                    dateText = "24/07/2025",
                    statusText = "Empréstimo finalizado"
                ),
                HistoryBook(
                    title = "Guerra e Paz",
                    author = "L. Tolstói",
                    coverRes = R.drawable.livro_guerra_e_paz,
                    availabilityText = "Disponível: físico + digital",
                    isAvailable = true,
                    dateText = "15/07/2025",
                    statusText = "Empréstimo finalizado"
                ),
                HistoryBook(
                    title = "Crime e Castigo",
                    author = "Fiódor Dostoiévski",
                    coverRes = R.drawable.livro_crime_e_castigo,
                    availabilityText = "Indisponível",
                    isAvailable = false,
                    dateText = "30/06/2025",
                    statusText = "Empréstimo finalizado"
                ),
                HistoryBook(
                    title = "O Livro dos Juízes",
                    author = "Desconhecido",
                    coverRes = R.drawable.livro_dos_juizes,
                    availabilityText = "Disponível: físico",
                    isAvailable = true,
                    dateText = "18/06/2025",
                    statusText = "Empréstimo finalizado"
                ),
                HistoryBook(
                    title = "Édipo Rei",
                    author = "Sófocles",
                    coverRes = R.drawable.livro_edipo_rei,
                    availabilityText = "Disponível: digital",
                    isAvailable = true,
                    dateText = "05/06/2025",
                    statusText = "Empréstimo finalizado"
                ),
                HistoryBook(
                    title = "Ilíada",
                    author = "Homero",
                    coverRes = R.drawable.livro_iliada,
                    availabilityText = "Indisponível",
                    isAvailable = false,
                    dateText = "22/05/2025",
                    statusText = "Empréstimo finalizado"
                ),
                HistoryBook(
                    title = "A Metamorfose",
                    author = "Franz Kafka",
                    coverRes = R.drawable.livro_metamorfose,
                    availabilityText = "Disponível: físico + digital",
                    isAvailable = true,
                    dateText = "10/05/2025",
                    statusText = "Empréstimo finalizado"
                ),
                HistoryBook(
                    title = "Napoleão",
                    author = "Andrew Roberts",
                    coverRes = R.drawable.livro_napoleao,
                    availabilityText = "Disponível: físico",
                    isAvailable = true,
                    dateText = "28/04/2025",
                    statusText = "Empréstimo finalizado"
                ),
                HistoryBook(
                    title = "Quincas Borba",
                    author = "Machado de Assis",
                    coverRes = R.drawable.livro_quincas,
                    availabilityText = "Disponível: físico",
                    isAvailable = true,
                    dateText = "15/04/2025",
                    statusText = "Empréstimo finalizado"
                ),
                HistoryBook(
                    title = "Rachel de Queiroz",
                    author = "Rachel de Queiroz",
                    coverRes = R.drawable.livro_rachelqueiroz,
                    availabilityText = "Disponível: físico",
                    isAvailable = true,
                    dateText = "01/04/2025",
                    statusText = "Empréstimo finalizado"
                ),
                HistoryBook(
                    title = "A Revolução dos Bichos",
                    author = "George Orwell",
                    coverRes = R.drawable.livro_rev_bichos,
                    availabilityText = "Indisponível",
                    isAvailable = false,
                    dateText = "20/03/2025",
                    statusText = "Empréstimo finalizado"
                ),
                HistoryBook(
                    title = "Romeu e Julieta (versão adaptada)",
                    author = "W. Carrasco",
                    coverRes = R.drawable.livro_romeu1,
                    availabilityText = "Disponível: digital",
                    isAvailable = true,
                    dateText = "05/03/2025",
                    statusText = "Empréstimo finalizado"
                ),
                HistoryBook(
                    title = "Sócrates",
                    author = "Desconhecido",
                    coverRes = R.drawable.livro_socrates,
                    availabilityText = "Disponível: físico",
                    isAvailable = true,
                    dateText = "18/02/2025",
                    statusText = "Empréstimo finalizado"
                ),
                HistoryBook(
                    title = "TCC Eletrônica Aplicada",
                    author = "M. Lopes",
                    coverRes = R.drawable.livro_tcc,
                    availabilityText = "Disponível: físico",
                    isAvailable = true,
                    dateText = "02/02/2025",
                    statusText = "Empréstimo finalizado"
                ),
                HistoryBook(
                    title = "Texto Acadêmico",
                    author = "Desconhecido",
                    coverRes = R.drawable.livro_texto_academico,
                    availabilityText = "Disponível: digital",
                    isAvailable = true,
                    dateText = "15/01/2025",
                    statusText = "Empréstimo finalizado"
                ),
                HistoryBook(
                    title = "Turma da Mônica",
                    author = "Mauricio de Sousa",
                    coverRes = R.drawable.livro_turmamonica,
                    availabilityText = "Disponível: físico",
                    isAvailable = true,
                    dateText = "05/01/2025",
                    statusText = "Empréstimo finalizado"
                ),
                HistoryBook(
                    title = "Diário de um Banana",
                    author = "Jeff Kinney",
                    coverRes = R.drawable.livro_banana,
                    availabilityText = "Disponível: físico",
                    isAvailable = true,
                    dateText = "20/12/2024",
                    statusText = "Empréstimo finalizado"
                )
            )
        )

        prepararPaginacao()
        renderPage()
        aplicarEstiloBotoes()
        configurarBuscaAnimada()

        // Paginação: < 1 >
        btnPagPrev.setOnClickListener {
            if (currentPage > 1) {
                irParaPagina(currentPage - 1)
            }
        }

        btnPagNext.setOnClickListener {
            if (currentPage < totalPages) {
                irParaPagina(currentPage + 1)
            }
        }
    }

    private fun configurarBuscaAnimada() {
        ivLupaHistorico.setOnClickListener {
            val mostrando = containerSearchHistorico.visibility == View.VISIBLE

            TransitionManager.beginDelayedTransition(rootLayout, AutoTransition())

            if (mostrando) {
                containerSearchHistorico.visibility = View.GONE
                esconderTeclado()
                etSearchHistorico.clearFocus()
            } else {
                containerSearchHistorico.visibility = View.VISIBLE
                etSearchHistorico.requestFocus()
                mostrarTeclado()
            }
        }
    }

    // Fecha barra de pesquisa se tocar fora dela
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN &&
            this::containerSearchHistorico.isInitialized &&
            containerSearchHistorico.visibility == View.VISIBLE
        ) {
            val rect = Rect()
            containerSearchHistorico.getGlobalVisibleRect(rect)

            val x = ev.rawX.toInt()
            val y = ev.rawY.toInt()

            if (!rect.contains(x, y)) {
                TransitionManager.beginDelayedTransition(rootLayout, AutoTransition())
                containerSearchHistorico.visibility = View.GONE
                esconderTeclado()
                etSearchHistorico.clearFocus()
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun mostrarTeclado() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(etSearchHistorico, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun esconderTeclado() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(etSearchHistorico.windowToken, 0)
    }

    private fun prepararPaginacao() {
        val n = allHistorico.size
        val needed = ceil(n / pageSize.toDouble()).toInt()
        totalPages = maxOf(needed, 1)
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
        val end = min(start + pageSize, allHistorico.size)
        val slice =
            if (start in 0 until end) allHistorico.subList(start, end) else emptyList()
        adapter.submitPage(slice)
        rvHistorico.scrollToPosition(0)
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
                    this@HistoricoEmprestimosUsuarioActivity,
                    android.R.color.black
                )
            )
            background = ContextCompat.getDrawable(
                this@HistoricoEmprestimosUsuarioActivity,
                R.drawable.bg_page_button_white
            )
        }

        btnPagPrev.config(
            texto = "<",
            habilitado = currentPage > 1,
            isCurrent = false
        )

        btnPagCenter.config(
            texto = currentPage.toString(),
            habilitado = true,
            isCurrent = true
        )

        btnPagNext.config(
            texto = ">",
            habilitado = currentPage < totalPages,
            isCurrent = false
        )
    }
}
