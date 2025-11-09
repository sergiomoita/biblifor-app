package com.example.biblifor

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.*
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.ktx.firestore

class CadastrarLivroAdministradorActivity : BaseActivity() {

    private lateinit var fb: FirebaseFirestore

    // ========= FUNÇÕES AUXILIARES =========
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

    // ====== Estilização ======
    private fun selecionar(btn: Button) {
        btn.setBackgroundColor(Color.parseColor("#002C9B"))
        btn.setTextColor(Color.WHITE)
    }

    private fun desselecionar(btn: Button) {
        btn.setBackgroundColor(Color.WHITE)
        btn.setTextColor(Color.parseColor("#002C9B"))
    }

    // ====== Disponibilidade → múltipla ======
    private fun configurarDisponibilidade(btn: Button) {
        btn.setOnClickListener {
            val selecionado = (btn.currentTextColor == Color.WHITE)

            if (selecionado) desselecionar(btn)
            else selecionar(btn)
        }
    }

    // ====== Empréstimo → exclusiva ======
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

    // =========================================
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
            startActivity(Intent(this, MenuPrincipalAdministradorActivity::class.java)); finish()
        }

        // ======= CAMPOS =======
        val etTitulo = findViewById<EditText>(R.id.lopesNome38)
        val etAutor = findViewById<EditText>(R.id.lopesAutor40)
        val etTopicos = findViewById<EditText>(R.id.lopesTopicos38)
        val etQtd = findViewById<EditText>(R.id.lopesQtd38)
        val etCodigoAcervo = findViewById<EditText>(R.id.lopesLocaliza38)


        // ====== BOTÕES ======
        val btnFisico = findViewById<Button>(R.id.btnDispoFisico)
        val btnOnline = findViewById<Button>(R.id.btnDispoOnline)
        val btnEmpSim = findViewById<Button>(R.id.btnEmprestarSim)
        val btnEmpNao = findViewById<Button>(R.id.btnEmprestarNao)

        // estilização inicial
        desselecionar(btnFisico)
        desselecionar(btnOnline)
        desselecionar(btnEmpSim)
        desselecionar(btnEmpNao)

        // disponibilidade → múltipla
        configurarDisponibilidade(btnFisico)
        configurarDisponibilidade(btnOnline)

        // empréstimo → exclusiva
        configurarEmprestimoExclusivo(btnEmpSim, btnEmpNao)


        // ====== BOTÃO CADASTRAR ======
        val btnCadastrar = findViewById<Button>(R.id.lopesBtnCadastrar38)
        btnCadastrar.setOnClickListener {

            try {
                validarCampo(etTitulo, "Título")
                validarCampo(etAutor, "Autor")
                validarCampo(etTopicos, "Tópicos")
                validarCampo(etQtd, "Quantidade de exemplares")
                validarCampo(etCodigoAcervo, "Código de acervo")

                // ====== Lógica de disponibilidade ======
                val disponibilidade = when {
                    btnFisico.currentTextColor == Color.WHITE &&
                            btnOnline.currentTextColor == Color.WHITE -> "Físico e Online"

                    btnFisico.currentTextColor == Color.WHITE -> "Físico"
                    btnOnline.currentTextColor == Color.WHITE -> "Online"
                    else -> "Indisponível"
                }

                // ====== Lógica de empréstimo ======
                val situacaoEmprestimo = when {
                    btnEmpSim.currentTextColor == Color.WHITE -> "Emprestável"
                    btnEmpNao.currentTextColor == Color.WHITE -> "Não-emprestável"
                    else -> "Não informado"
                }

                // imagem placeholder
                val imagemString = "imagem aqui"

                val dadosLivro = mapOf(
                    "Titulo" to etTitulo.text.toString(),
                    "Autor" to etAutor.text.toString(),
                    "Topicos" to etTopicos.text.toString(),
                    "QuantidadeExemplares" to etQtd.text.toString(),
                    "CodigoAcervo" to etCodigoAcervo.text.toString(),
                    "Disponibilidade" to disponibilidade,
                    "SituacaoEmprestimo" to situacaoEmprestimo,
                    "Imagem" to imagemString
                )

                fb.collection("livros")
                    .document(etTitulo.text.toString())   // título como ID
                    .set(dadosLivro)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Livro cadastrado com sucesso!", Toast.LENGTH_LONG).show()
                        startActivity(Intent(this, ConfirmacaoCadastroAdministradorActivity::class.java))
                    }
                    .addOnFailureListener {
                        showErrorToast("Erro ao cadastrar livro.")
                    }

            } catch (e: IllegalArgumentException) {
                showErrorToast(e.message ?: "Erro de validação.")
            } catch (e: Exception) {
                showErrorToast("Erro inesperado.")
            }
        }
    }
}
