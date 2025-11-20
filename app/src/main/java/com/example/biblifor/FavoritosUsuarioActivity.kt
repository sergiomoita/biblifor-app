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

    // Busca
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

        adapter = FavoritosPagedAdapter { /* ação ao clicar no favorito */ }

        rvFavoritos.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvFavoritos.adapter = adapter
        rvFavoritos.setHasFixedSize(true)

        configurarBuscaAnimada()

        btnPag1.setOnClickListener {
            if (currentPage > 1) irParaPagina(currentPage - 1)
        }
        btnPag3.setOnClickListener {
            if (currentPage < totalPages) irParaPagina(currentPage + 1)
        }

        // 🔥 Carregar do Firestore
        carregarLivrosDoFirebase()
    }

    private fun carregarLivrosDoFirebase() {
        db.collection("livros")
            .get()
            .addOnSuccessListener { result ->
                allFavoritos.clear()

                for (doc in result) {
                    val titulo = doc.getString("Titulo") ?: continue
                    val autor = doc.getString("Autor") ?: ""
                    val situacaoEmprestimo = doc.getString("SituacaoEmprestimo") ?: ""
                    val imagemBase64 = doc.getString("Imagem")

                    val emprestavel =
                        situacaoEmprestimo.equals("Emprestável", ignoreCase = true)

                    val tituloComAutor =
                        if (autor.isNotBlank()) "$titulo - $autor" else titulo

                    val livro = Book(
                        title = tituloComAutor,
                        coverRes = R.drawable.livro_1984,
                        emprestavel = emprestavel,
                        imagemBase64 = imagemBase64
                    )
                    allFavoritos.add(livro)
                }

                prepararPaginacao()
                renderPage()
                aplicarEstiloBotoes()
            }
            .addOnFailureListener {
                prepararPaginacao()
                renderPage()
                aplicarEstiloBotoes()
            }
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

        btnPag1.config("<", currentPage > 1, false)
        btnPag2.config(currentPage.toString(), true, true)
        btnPag3.config(">", currentPage < totalPages, false)
    }
}
