package com.example.biblifor

import android.graphics.Typeface
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.ceil
import kotlin.math.min

class FavoritosUsuarioActivity : AppCompatActivity() {

    private lateinit var rvFavoritos: RecyclerView
    private lateinit var btnPag1: Button
    private lateinit var btnPag2: Button
    private lateinit var btnPag3: Button
    private lateinit var adapter: FavoritosPagedAdapter

    private val allFavoritos = mutableListOf<Book>()
    private val pageSize = 5
    private var currentPage = 1
    private var totalPages = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favoritos_usuario)

        rvFavoritos = findViewById(R.id.rvFavoritos)
        btnPag1 = findViewById(R.id.btnPag1Favoritos)
        btnPag2 = findViewById(R.id.btnPag2Favoritos)
        btnPag3 = findViewById(R.id.btnPag3Favoritos)

        adapter = FavoritosPagedAdapter { book ->
            // TODO: ação ao clicar
        }

        rvFavoritos.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvFavoritos.adapter = adapter
        rvFavoritos.setHasFixedSize(true)

        allFavoritos.addAll(gerarMockComSeusDrawables())

        prepararPaginacao()
        renderPage()
        aplicarEstiloBotoes()

        btnPag1.setOnClickListener { irParaPagina(1) }
        btnPag2.setOnClickListener { irParaPagina(2) }
        btnPag3.setOnClickListener { irParaPagina(3) }
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
        fun Button.ativar(ativo: Boolean, habilitado: Boolean) {
            isEnabled = habilitado
            typeface = if (ativo) Typeface.DEFAULT_BOLD else Typeface.DEFAULT
            alpha = when {
                !habilitado -> 0.35f
                ativo -> 1f
                else -> 0.95f
            }
            setTextColor(ContextCompat.getColor(this@FavoritosUsuarioActivity, android.R.color.black))
            background = ContextCompat.getDrawable(this@FavoritosUsuarioActivity, R.drawable.bg_page_button_white)
        }

        btnPag1.ativar(currentPage == 1, totalPages >= 1)
        btnPag2.ativar(currentPage == 2, totalPages >= 2)
        btnPag3.ativar(currentPage == 3, totalPages >= 3)
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
