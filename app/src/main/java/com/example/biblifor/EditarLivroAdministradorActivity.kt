package com.example.biblifor

import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.util.Base64
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.biblifor.util.bitmapToBase64
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class EditarLivroAdministradorActivity : BaseActivity() {

    private val db = Firebase.firestore

    private lateinit var imgCapa: ImageView
    private lateinit var btnEditarImagem: ImageView

    private lateinit var etNome: EditText
    private lateinit var etAutor: EditText
    private lateinit var etLocaliza: EditText

    private lateinit var btnFisico: Button
    private lateinit var btnOnline: Button
    private lateinit var btnEmpSim: Button
    private lateinit var btnEmpNao: Button
    private lateinit var btnRecSim: Button
    private lateinit var btnRecNao: Button

    private var imagemLivroBase64: String? = null
    private lateinit var livroId: String

    // Seleção de nova imagem
    private val seletorImagemLivro =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                val input = contentResolver.openInputStream(uri)
                val bitmap = BitmapFactory.decodeStream(input)
                input?.close()

                if (bitmap != null) {
                    imagemLivroBase64 = bitmapToBase64(bitmap)
                    imgCapa.setImageBitmap(bitmap)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_editar_livro_administrador)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        livroId = intent.getStringExtra("livroId") ?: ""

        imgCapa = findViewById(R.id.lopesCapaEditar)
        btnEditarImagem = findViewById(R.id.btnEditarImagem)

        etNome = findViewById(R.id.lopesNome46)
        etAutor = findViewById(R.id.lopesAutor46)
        etLocaliza = findViewById(R.id.lopesLocaliza46)

        btnFisico = findViewById(R.id.btnDispoFisico)
        btnOnline = findViewById(R.id.btnDispoOnline)
        btnEmpSim = findViewById(R.id.btnEmprestarSim)
        btnEmpNao = findViewById(R.id.btnEmprestarNao)
        btnRecSim = findViewById(R.id.btnRecomendarSim)
        btnRecNao = findViewById(R.id.btnRecomendarNao)

        configurarDisponibilidade(btnFisico)
        configurarDisponibilidade(btnOnline)
        configurarEmprestimoExclusivo(btnEmpSim, btnEmpNao)
        configurarEmprestimoExclusivo(btnRecSim, btnRecNao)

        btnEditarImagem.setOnClickListener {
            seletorImagemLivro.launch("image/*")
        }

        if (livroId.isNotEmpty()) carregarLivro(livroId)

        findViewById<Button>(R.id.lopesBtnCadastrar46).setOnClickListener {
            atualizarLivro()
        }

        findViewById<ImageView>(R.id.lopesSetaVoltar39).setOnClickListener {
            finish()
        }
    }

    // -------- CARREGAR DADOS ----------------------------------------------------------------------------

    private fun carregarLivro(id: String) {
        db.collection("livros")
            .document(id)
            .get()
            .addOnSuccessListener { doc ->

                etNome.setText(doc.getString("Titulo") ?: "")
                etAutor.setText(doc.getString("Autor") ?: "")
                etLocaliza.setText(doc.getString("CodigoAcervo") ?: "")

                when (doc.getString("Disponibilidade") ?: "") {
                    "Físico e Online" -> {
                        selecionar(btnFisico)
                        selecionar(btnOnline)
                    }
                    "Físico" -> selecionar(btnFisico)
                    "Online" -> selecionar(btnOnline)
                }

                when (doc.getString("SituacaoEmprestimo") ?: "") {
                    "Emprestável" -> selecionar(btnEmpSim)
                    "Não-emprestável" -> selecionar(btnEmpNao)
                }

                if (doc.getBoolean("recomendar") == true)
                    selecionar(btnRecSim)
                else
                    selecionar(btnRecNao)

                val imgBase64 = doc.getString("Imagem")
                imagemLivroBase64 = imgBase64

                if (!imgBase64.isNullOrEmpty()) {
                    try {
                        val bytes = Base64.decode(imgBase64, Base64.DEFAULT)
                        val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                        imgCapa.setImageBitmap(bmp)
                    } catch (_: Exception) {}
                }
            }
    }

    // -------- BOTÕES ----------------------------------------------------------------------------

    private fun selecionar(btn: Button) {
        btn.setBackgroundColor(Color.parseColor("#002C9B"))
        btn.setTextColor(Color.WHITE)
    }

    private fun desselecionar(btn: Button) {
        btn.setBackgroundColor(Color.WHITE)
        btn.setTextColor(Color.parseColor("#002C9B"))
    }

    private fun configurarDisponibilidade(btn: Button) {
        btn.setOnClickListener {
            if (btn.currentTextColor == Color.WHITE) desselecionar(btn)
            else selecionar(btn)
        }
    }

    private fun configurarEmprestimoExclusivo(btnSim: Button, btnNao: Button) {
        btnSim.setOnClickListener {
            selecionar(btnSim)
            desselecionar(btnNao)
        }

        btnNao.setOnClickListener {
            selecionar(btnNao)
            desselecionar(btnSim)
        }
    }

    // -------- ATUALIZAR ----------------------------------------------------------------------------

    private fun atualizarLivro() {

        val disponibilidade = when {
            btnFisico.currentTextColor == Color.WHITE &&
                    btnOnline.currentTextColor == Color.WHITE -> "Físico e Online"
            btnFisico.currentTextColor == Color.WHITE -> "Físico"
            btnOnline.currentTextColor == Color.WHITE -> "Online"
            else -> "Indisponível"
        }

        val emprestar = when {
            btnEmpSim.currentTextColor == Color.WHITE -> "Emprestável"
            btnEmpNao.currentTextColor == Color.WHITE -> "Não-emprestável"
            else -> "Não informado"
        }

        val recomendar = (btnRecSim.currentTextColor == Color.WHITE)

        val dados = mapOf(
            "Titulo" to etNome.text.toString(),
            "Autor" to etAutor.text.toString(),
            "CodigoAcervo" to etLocaliza.text.toString(),
            "Disponibilidade" to disponibilidade,
            "SituacaoEmprestimo" to emprestar,
            "recomendar" to recomendar,
            "Imagem" to (imagemLivroBase64 ?: "")
        )

        db.collection("livros")
            .document(livroId)
            .update(dados)
            .addOnSuccessListener {
                Toast.makeText(this, "Livro atualizado!", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Erro ao atualizar.", Toast.LENGTH_SHORT).show()
            }
    }
}
