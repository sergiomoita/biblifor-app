package com.example.biblifor

import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.biblifor.util.bitmapToBase64
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CadastrarLivroAdministradorActivity : BaseActivity() {

    private lateinit var fb: FirebaseFirestore

    // IMAGEM DO LIVRO
    private lateinit var btnImagemLivro: Button
    private var imagemLivroBase64: String? = null

    private val seletorImagemLivro =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                val input = contentResolver.openInputStream(uri)
                val bitmap = BitmapFactory.decodeStream(input)
                input?.close()

                if (bitmap != null) {
                    imagemLivroBase64 = bitmapToBase64(bitmap)
                    btnImagemLivro.text = "Imagem selecionada"
                }
            }
        }

    private fun validarCampo(et: EditText, label: String) {
        if (et.text.toString().trim().isEmpty())
            throw IllegalArgumentException("$label não pode estar vazio")
    }

    private fun showErrorToast(message: String) {
        val inflater: LayoutInflater = layoutInflater
        val view = inflater.inflate(R.layout.toast_excecao_cadastro, null)
        view.findViewById<TextView>(R.id.toast_message).text = message

        Toast(this).apply {
            duration = Toast.LENGTH_LONG
            setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 120)
            this.view = view
            show()
        }
    }

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
            val selecionado = (btn.currentTextColor == Color.WHITE)

            if (selecionado) desselecionar(btn)
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastrar_livro_administrador)

        fb = Firebase.firestore

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // ======= TOP BAR =======
        findViewById<ImageView>(R.id.lopesSetaVoltar38).setOnClickListener {
            startActivity(Intent(this, MenuPrincipalAdministradorActivity::class.java))
        }
        findViewById<ImageView>(R.id.lopesEscrever38).setOnClickListener {
            startActivity(Intent(this, EscreverMensagemAdministradorActivity::class.java))
        }

        // ======= BOTTOM BAR =======
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
            startActivity(Intent(this, MenuHamburguerAdministradorActivity::class.java)); finish()
        }

        // ======= CAMPOS =======
        val etTitulo = findViewById<EditText>(R.id.lopesNome38)
        val etAutor = findViewById<EditText>(R.id.lopesAutor40)
        val etCodigoAcervo = findViewById<EditText>(R.id.lopesLocaliza38)

        btnImagemLivro = findViewById(R.id.lopesBtnImagem38)
        btnImagemLivro.setOnClickListener {
            seletorImagemLivro.launch("image/*")
        }

        // ====== BOTÕES ======
        val btnFisico = findViewById<Button>(R.id.btnDispoFisico)
        val btnOnline = findViewById<Button>(R.id.btnDispoOnline)
        val btnEmpSim = findViewById<Button>(R.id.btnEmprestarSim)
        val btnEmpNao = findViewById<Button>(R.id.btnEmprestarNao)

        val btnRecSim = findViewById<Button>(R.id.btnRecomendarSim)
        val btnRecNao = findViewById<Button>(R.id.btnRecomendarNao)

        desselecionar(btnFisico)
        desselecionar(btnOnline)
        desselecionar(btnEmpSim)
        desselecionar(btnEmpNao)
        desselecionar(btnRecSim)
        desselecionar(btnRecNao)

        configurarDisponibilidade(btnFisico)
        configurarDisponibilidade(btnOnline)
        configurarEmprestimoExclusivo(btnEmpSim, btnEmpNao)
        configurarEmprestimoExclusivo(btnRecSim, btnRecNao)

        val btnCadastrar = findViewById<Button>(R.id.lopesBtnCadastrar38)
        btnCadastrar.setOnClickListener {

            try {
                validarCampo(etTitulo, "Título")
                validarCampo(etAutor, "Autor")
                validarCampo(etCodigoAcervo, "Código de acervo")

                val disponibilidade = when {
                    btnFisico.currentTextColor == Color.WHITE &&
                            btnOnline.currentTextColor == Color.WHITE -> "Físico e Online"
                    btnFisico.currentTextColor == Color.WHITE -> "Físico"
                    btnOnline.currentTextColor == Color.WHITE -> "Online"
                    else -> "Indisponível"
                }

                val situacaoEmprestimo = when {
                    btnEmpSim.currentTextColor == Color.WHITE -> "Emprestável"
                    btnEmpNao.currentTextColor == Color.WHITE -> "Não-emprestável"
                    else -> "Não informado"
                }

                val recomendar = (btnRecSim.currentTextColor == Color.WHITE)
                val imagemString = imagemLivroBase64 ?: ""

                val dadosLivro = mapOf(
                    "Titulo" to etTitulo.text.toString(),
                    "Autor" to etAutor.text.toString(),
                    "CodigoAcervo" to etCodigoAcervo.text.toString(),
                    "Disponibilidade" to disponibilidade,
                    "SituacaoEmprestimo" to situacaoEmprestimo,
                    "Imagem" to imagemString,
                    "recomendar" to recomendar
                )

                val nomeLivro = etTitulo.text.toString()

                val builder = androidx.appcompat.app.AlertDialog.Builder(this)
                builder.setTitle("Confirmação")
                builder.setMessage("Gostaria de cadastrar o livro \"$nomeLivro\"?")

                builder.setPositiveButton("Sim") { _, _ ->
                    fb.collection("livros")
                        .document(nomeLivro)
                        .set(dadosLivro)
                        .addOnSuccessListener {
                            Toast.makeText(
                                this,
                                "Livro cadastrado com sucesso!",
                                Toast.LENGTH_LONG
                            ).show()
                            startActivity(
                                Intent(
                                    this,
                                    MenuPrincipalAdministradorActivity::class.java
                                )
                            ); finish()
                        }
                        .addOnFailureListener {
                            showErrorToast("Erro ao cadastrar livro.")
                        }
                }

                builder.setNegativeButton("Não") { dialog, _ ->
                    dialog.dismiss()
                }

                builder.create().show()

            } catch (e: IllegalArgumentException) {
                showErrorToast(e.message ?: "Erro de validação.")
            } catch (e: Exception) {
                showErrorToast("Erro inesperado.")
            }
        }
    }
}
