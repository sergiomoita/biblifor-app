package com.example.biblifor

import BookAdapterEmprestaveisAdmin
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.graphics.Typeface
import android.os.Bundle
import android.text.TextWatcher
import android.text.Editable
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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.math.ceil
import kotlin.math.min

class LivrosEmprestaveisAdministradorActivity : BaseActivity() {

    private lateinit var rootLayout: ConstraintLayout

    private lateinit var rvLivros: RecyclerView
    private lateinit var btnPagPrev: TextView
    private lateinit var btnPagCenter: TextView
    private lateinit var btnPagNext: TextView

    // Busca
    private lateinit var ivLupaLivros: ImageView
    private lateinit var containerSearchLivros: View
    private lateinit var etSearchLivros: EditText

    private val allLivros = mutableListOf<Book>()
    private val pageSize = 8   // 🔥 agora 8 por página (2 colunas × 4 linhas)
    private var currentPage = 1
    private var totalPages = 1

    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_livros_emprestaveis_administrador)

        rootLayout = findViewById(R.id.main)
        db = Firebase.firestore

        // Voltar
        findViewById<ImageView>(R.id.leoImagemSetaPU5).setOnClickListener {
            startActivity(Intent(this, MenuPrincipalAdministradorActivity::class.java))
            finish()
        }

        // Barra inferior
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

        configurarBuscaAnimada()
        configurarFiltroTexto()

        btnPagPrev.setOnClickListener {
            if (currentPage > 1) irParaPagina(currentPage - 1)
        }
        btnPagNext.setOnClickListener {
            if (currentPage < totalPages) irParaPagina(currentPage + 1)
        }

        carregarLivrosDoFirebase()
    }

    private fun carregarLivrosDoFirebase() {
        db.collection("livros")
            .get()
            .addOnSuccessListener { result ->
                allLivros.clear()

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

                    allLivros.add(livro)
                }

                allLivros.sortBy { it.title.lowercase() }

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

    // 🔥 FILTRO DE TEXTO APLICADO EM TEMPO REAL
    private fun configurarFiltroTexto() {
        etSearchLivros.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                aplicarFiltroBusca(s.toString())
            }
        })
    }

    // 🔥 Filtro que puxa resultados para o topo
    private fun aplicarFiltroBusca(texto: String) {

        val termo = texto.trim().lowercase()

        if (termo.isEmpty()) {
            allLivros.sortBy { it.title.lowercase() }
        } else {
            val combinam = allLivros.filter {
                it.title.lowercase().contains(termo)
            }

            val naoCombinam = allLivros.filter {
                !it.title.lowercase().contains(termo)
            }

            allLivros.clear()
            allLivros.addAll(combinam + naoCombinam)
        }

        currentPage = 1
        prepararPaginacao()
        renderPage()
        aplicarEstiloBotoes()
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

        rvLivros.adapter = BookAdapterEmprestaveisAdmin(slice) { livro ->
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
