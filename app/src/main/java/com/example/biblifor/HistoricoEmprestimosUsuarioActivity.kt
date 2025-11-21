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
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.math.ceil
import kotlin.math.min

class HistoricoEmprestimosUsuarioActivity : BaseActivity() {

    private lateinit var rootLayout: ConstraintLayout

    private lateinit var rvHistorico: RecyclerView
    private lateinit var btnPagPrev: TextView
    private lateinit var btnPagCenter: TextView
    private lateinit var btnPagNext: TextView

    private lateinit var adapter: FavoritosPagedAdapter

    // Busca
    private lateinit var ivLupaHistorico: ImageView
    private lateinit var containerSearchHistorico: View
    private lateinit var etSearchHistorico: EditText

    private val allHistorico = mutableListOf<Book>()
    private val pageSize = 5
    private var currentPage = 1
    private var totalPages = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historico_emprestimos_usuario)

        rootLayout = findViewById(R.id.main)

        // ==============================
        // Barra inferior
        // ==============================
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
        findViewById<ImageView>(R.id.btnVoltarHistorico).setOnClickListener { finish() }

        rvHistorico = findViewById(R.id.rvHistorico)
        btnPagPrev = findViewById(R.id.btnPagPrevHistorico)
        btnPagCenter = findViewById(R.id.btnPagCenterHistorico)
        btnPagNext = findViewById(R.id.btnPagNextHistorico)

        ivLupaHistorico = findViewById(R.id.ivLupaHistorico)
        containerSearchHistorico = findViewById(R.id.containerSearchHistorico)
        etSearchHistorico = findViewById(R.id.etSearchHistorico)

        // Usa o mesmo adapter das outras telas
        adapter = FavoritosPagedAdapter { book ->
            // Se quiser abrir um popup específico de histórico, troque a Activity aqui
            val intent = Intent(this, PopupResultadosUsuarioActivity::class.java)
            intent.putExtra("livroId", book.livroId)
            intent.putExtra("titulo", book.tituloOriginal)
            intent.putExtra("autor", book.autor)
            intent.putExtra("imagemBase64", book.imagemBase64)
            intent.putExtra("situacao", book.situacaoEmprestimo)
            intent.putExtra("disponibilidade", book.disponibilidade)
            startActivity(intent)
        }

        rvHistorico.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvHistorico.adapter = adapter
        rvHistorico.setHasFixedSize(true)

        configurarBuscaAnimada()
        carregarHistoricoDoFirebase()

        btnPagPrev.setOnClickListener {
            if (currentPage > 1) irParaPagina(currentPage - 1)
        }

        btnPagNext.setOnClickListener {
            if (currentPage < totalPages) irParaPagina(currentPage + 1)
        }
    }

    // ==============================
    //   CARREGAR HISTÓRICO DO ALUNO
    // ==============================
    private fun carregarHistoricoDoFirebase() {
        val db = Firebase.firestore

        val prefs = getSharedPreferences("APP_PREFS", MODE_PRIVATE)
        val matricula = prefs.getString("MATRICULA_USER", "") ?: ""

        if (matricula.isEmpty()) return

        db.collection("alunos")
            .document(matricula)
            .collection("historicoEmprestimos")
            .orderBy("dataEmprestimo", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->

                allHistorico.clear()

                for (doc in result) {

                    val livroId = doc.getString("livroId") ?: doc.id
                    val titulo = doc.getString("titulo") ?: livroId
                    val autor = doc.getString("autor") ?: ""

                    // Agora vamos buscar os dados reais do livro
                    db.collection("livros")
                        .document(livroId)
                        .get()
                        .addOnSuccessListener { livroDoc ->

                            val imagemBase64 = livroDoc.getString("Imagem")
                            val situacaoEmprest = livroDoc.getString("SituacaoEmprestimo") ?: ""
                            val disponibilidade = livroDoc.getString("Disponibilidade") ?: ""

                            val tituloComAutor =
                                if (autor.isNotBlank()) "$titulo - $autor" else titulo

                            val emprestavel =
                                situacaoEmprest.equals("Emprestável", ignoreCase = true)

                            val book = Book(
                                title = tituloComAutor,
                                coverRes = R.drawable.livro_socrates, // fallback
                                emprestavel = emprestavel,
                                imagemBase64 = imagemBase64,
                                tituloOriginal = titulo,
                                autor = autor,
                                situacaoEmprestimo = situacaoEmprest,
                                disponibilidade = disponibilidade,
                                livroId = livroId
                            )

                            allHistorico.add(book)

                            prepararPaginacao()
                            renderPage()
                            aplicarEstiloBotoes()

                        }
                }
            }
    }


    // ==============================
    //   BUSCA + ANIMAÇÃO
    // ==============================
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

        etSearchHistorico.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val termo = s?.toString()?.trim()?.lowercase().orEmpty()

                if (termo.isEmpty()) {
                    prepararPaginacao()
                    renderPage()
                    aplicarEstiloBotoes()
                    return
                }

                val filtrados = allHistorico
                    .filter { it.title.lowercase().contains(termo) }
                    .sortedBy { it.title }

                adapter.submitPage(filtrados)
            }
        })
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN &&
            this::containerSearchHistorico.isInitialized &&
            containerSearchHistorico.visibility == View.VISIBLE
        ) {
            val rect = Rect()
            containerSearchHistorico.getGlobalVisibleRect(rect)
            if (!rect.contains(ev.rawX.toInt(), ev.rawY.toInt())) {
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

    // ==============================
    //   PAGINAÇÃO
    // ==============================
    private fun prepararPaginacao() {
        val n = allHistorico.size
        val needed = ceil(n / pageSize.toDouble()).toInt()
        totalPages = maxOf(needed, 1)
        currentPage = currentPage.coerceIn(1, totalPages)
    }

    private fun irParaPagina(p: Int) {
        if (p in 1..totalPages) {
            currentPage = p
            renderPage()
            aplicarEstiloBotoes()
        }
    }

    private fun renderPage() {
        val start = (currentPage - 1) * pageSize
        val end = min(start + pageSize, allHistorico.size)

        val slice =
            if (start in 0 until end) allHistorico.subList(start, end)
            else emptyList()

        adapter.submitPage(slice)
        rvHistorico.scrollToPosition(0)
    }

    private fun aplicarEstiloBotoes() {
        fun TextView.estilizar(texto: String, habilitado: Boolean, atual: Boolean) {
            text = texto
            isEnabled = habilitado
            typeface = if (atual) Typeface.DEFAULT_BOLD else Typeface.DEFAULT
            alpha = if (habilitado) 1f else 0.35f
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

        btnPagPrev.estilizar("<", currentPage > 1, false)
        btnPagCenter.estilizar(currentPage.toString(), true, true)
        btnPagNext.estilizar(">", currentPage < totalPages, false)
    }
}
