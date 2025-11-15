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
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.ceil
import kotlin.math.min

class LivrosEmprestaveisAdministradorActivity : BaseActivity() {

    private lateinit var rootLayout: ConstraintLayout

    private lateinit var rvLivros: RecyclerView
    private lateinit var btnPagPrev: TextView      // "<"
    private lateinit var btnPagCenter: TextView   // número
    private lateinit var btnPagNext: TextView     // ">"

    // Busca
    private lateinit var ivLupaLivros: ImageView
    private lateinit var containerSearchLivros: View
    private lateinit var etSearchLivros: EditText

    private val allLivros = mutableListOf<Book>()
    private val pageSize = 6        // 6 itens por página → 2 colunas x 3 linhas
    private var currentPage = 1
    private var totalPages = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_livros_emprestaveis_administrador)

        rootLayout = findViewById(R.id.main)

        // Voltar
        findViewById<ImageView>(R.id.leoImagemSetaPU5).setOnClickListener {
            startActivity(Intent(this, MenuPrincipalAdministradorActivity::class.java))
            finish()
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

        rvLivros = findViewById(R.id.rvLivros)
        rvLivros.layoutManager = GridLayoutManager(this, 2)
        rvLivros.setHasFixedSize(true)

        btnPagPrev = findViewById(R.id.btnPagPrevLivrosEmp)
        btnPagCenter = findViewById(R.id.btnPagCenterLivrosEmp)
        btnPagNext = findViewById(R.id.btnPagNextLivrosEmp)

        ivLupaLivros = findViewById(R.id.ivLupaLivrosEmprestaveis)
        containerSearchLivros = findViewById(R.id.containerSearchLivrosEmprestaveis)
        etSearchLivros = findViewById(R.id.etSearchLivrosEmprestaveis)

        // 🔹 Mesmos livros usados nas telas de Favoritos / Recomendados
        allLivros.addAll(
            listOf(
                Book("1984 - George Orwell",          R.drawable.livro_1984,            true),
                Book("Antígona - Sófocles",           R.drawable.livro_antigona,        true),
                Book("Diário de um Banana – 1",       R.drawable.livro_banana,          false),
                Book("Código Limpo - Robert C.",      R.drawable.livro_cleancode,       true),
                Book("Crime e Castigo - F. Dostoiévski", R.drawable.livro_crime_e_castigo, false),
                Book("Dom Casmurro - M. de Assis",    R.drawable.livro_dom_casmurro,    true),
                Book("O Livro dos Juízes",            R.drawable.livro_dos_juizes,      true),
                Book("Édipo Rei - Sófocles",          R.drawable.livro_edipo_rei,       true),
                Book("Guerra e Paz - L. Tolstói",     R.drawable.livro_guerra_e_paz,    true),
                Book("Ilíada - Homero",               R.drawable.livro_iliada,          false),
                Book("A Metamorfose - F. Kafka",      R.drawable.livro_metamorfose,     true),
                Book("Napoleão - Biografia",          R.drawable.livro_napoleao,        true),
                Book("Quincas Borba - M. de Assis",   R.drawable.livro_quincas,         false),
                Book("Rachel de Queiroz - Obras",     R.drawable.livro_rachelqueiroz,   true),
                Book("A Revolução dos Bichos",        R.drawable.livro_rev_bichos,      true),
                Book("Romeu e Julieta - W. Shakespeare (Versão 1)", R.drawable.livro_romeu1, true),
                Book("Romeu e Julieta - W. Shakespeare (Versão 2)", R.drawable.livro_romeu2, true),
                Book("A Apologia de Sócrates",        R.drawable.livro_socrates,        true),
                Book("TCC Eletrônica Aplicada - M. Lopes", R.drawable.livro_tcc,       true),
                Book("Texto Acadêmico - Referências", R.drawable.livro_texto_academico, true),
                Book("Turma da Mônica - Coleção",     R.drawable.livro_turmamonica,     true)
            )
        )

        prepararPaginacao()
        renderPage()
        aplicarEstiloBotoes()
        configurarBuscaAnimada()

        // Paginação < 1 >
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
        ivLupaLivros.setOnClickListener {
            val mostrando = containerSearchLivros.visibility == View.VISIBLE

            TransitionManager.beginDelayedTransition(rootLayout, AutoTransition())

            if (mostrando) {
                containerSearchLivros.visibility = View.GONE
                esconderTeclado()
                etSearchLivros.clearFocus()
            } else {
                containerSearchLivros.visibility = View.VISIBLE
                etSearchLivros.requestFocus()
                mostrarTeclado()
            }
        }
    }

    // Fecha barra de busca se tocar fora dela
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN &&
            this::containerSearchLivros.isInitialized &&
            containerSearchLivros.visibility == View.VISIBLE
        ) {
            val rect = Rect()
            containerSearchLivros.getGlobalVisibleRect(rect)

            val x = ev.rawX.toInt()
            val y = ev.rawY.toInt()

            if (!rect.contains(x, y)) {
                TransitionManager.beginDelayedTransition(rootLayout, AutoTransition())
                containerSearchLivros.visibility = View.GONE
                esconderTeclado()
                etSearchLivros.clearFocus()
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun mostrarTeclado() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(etSearchLivros, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun esconderTeclado() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(etSearchLivros.windowToken, 0)
    }

    private fun prepararPaginacao() {
        val n = allLivros.size
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
        val end = min(start + pageSize, allLivros.size)
        val slice = if (start in 0 until end) allLivros.subList(start, end) else emptyList()

        rvLivros.adapter = BookAdapter(slice) { livro ->
            if (livro.title.contains("Crime e Castigo", ignoreCase = true)) {
                startActivity(Intent(this, EmprestarLivroAdministradorActivity::class.java))
            }
        }
        rvLivros.scrollToPosition(0)
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
                    this@LivrosEmprestaveisAdministradorActivity,
                    android.R.color.black
                )
            )
            background = ContextCompat.getDrawable(
                this@LivrosEmprestaveisAdministradorActivity,
                R.drawable.bg_page_button_white
            )
        }

        btnPagPrev.config("<", currentPage > 1, false)
        btnPagCenter.config(currentPage.toString(), true, true)
        btnPagNext.config(">", currentPage < totalPages, false)
    }
}
