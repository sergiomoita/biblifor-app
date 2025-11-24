package com.example.biblifor

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import com.example.biblifor.util.bitmapToBase64
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CadastroUsuarioActivity : BaseActivity() {

    private enum class Tipo { ALUNO, ADMIN }

    private lateinit var btnAluno: Button
    private lateinit var btnAdmin: Button
    private lateinit var groupAluno: LinearLayout
    private lateinit var groupAdmin: LinearLayout
    private lateinit var btnCadastrar: Button

    private lateinit var imgFotoPerfilAluno: ImageView
    private lateinit var imgFotoPerfilAdmin: ImageView
    private var fotoAlunoBase64: String? = null
    private var fotoAdminBase64: String? = null

    private var tipoSelecionado: Tipo? = null
    private val db = Firebase.firestore

    private val seletorImagemAluno =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                val input = contentResolver.openInputStream(uri)
                val bitmap = BitmapFactory.decodeStream(input)
                input?.close()

                if (bitmap != null) {
                    imgFotoPerfilAluno.setImageBitmap(bitmap)
                    fotoAlunoBase64 = bitmapToBase64(bitmap)
                }
            }
        }

    private val seletorImagemAdmin =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                val input = contentResolver.openInputStream(uri)
                val bitmap = BitmapFactory.decodeStream(input)
                input?.close()

                if (bitmap != null) {
                    imgFotoPerfilAdmin.setImageBitmap(bitmap)
                    fotoAdminBase64 = bitmapToBase64(bitmap)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_usuario)

        btnAluno = findViewById(R.id.btnAluno)
        btnAdmin = findViewById(R.id.btnAdministrador)
        groupAluno = findViewById(R.id.groupAluno)
        groupAdmin = findViewById(R.id.groupAdministrador)
        btnCadastrar = findViewById(R.id.btnAcessarLoginUsuarioSergio)

        imgFotoPerfilAluno = findViewById(R.id.imgFotoPerfilAluno)
        imgFotoPerfilAdmin = findViewById(R.id.imgFotoPerfilAdmin)

        val btnVoltarCadastro: ImageView = findViewById(R.id.btnVoltarCadastro)
        btnVoltarCadastro.setOnClickListener {
            val intent = Intent(this, LoginUsuarioActivity::class.java)
            startActivity(intent)
            finish()
        }

        imgFotoPerfilAluno.setOnClickListener { seletorImagemAluno.launch("image/*") }
        imgFotoPerfilAdmin.setOnClickListener { seletorImagemAdmin.launch("image/*") }

        btnAluno.setOnClickListener {
            tipoSelecionado = Tipo.ALUNO
            groupAluno.visibility = View.VISIBLE
            groupAdmin.visibility = View.GONE
            destacarSelecionado(btnAluno, btnAdmin)
        }

        btnAdmin.setOnClickListener {
            tipoSelecionado = Tipo.ADMIN
            groupAluno.visibility = View.GONE
            groupAdmin.visibility = View.VISIBLE
            destacarSelecionado(btnAdmin, btnAluno)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            findViewById<EditText>(R.id.inputSenhaAluno).importantForAutofill =
                View.IMPORTANT_FOR_AUTOFILL_NO
            findViewById<EditText>(R.id.inputSenhaAdmin).importantForAutofill =
                View.IMPORTANT_FOR_AUTOFILL_NO
        }

        btnCadastrar.setOnClickListener {
            when (tipoSelecionado) {
                Tipo.ALUNO -> cadastrarAluno()
                Tipo.ADMIN -> cadastrarAdmin()
                null -> toast("Selecione Aluno ou Administrador.")
            }
        }
    }

    // ============================================================
    // ========================   ALUNO   =========================
    // ============================================================
    private fun cadastrarAluno() {
        val curso = findViewById<EditText>(R.id.inputCursoAluno).text.toString()
        val matricula = findViewById<EditText>(R.id.inputMatriculaAluno).text.toString()
        val nome = findViewById<EditText>(R.id.inputNomeAluno).text.toString()
        val senha = findViewById<EditText>(R.id.inputSenhaAluno).text.toString()

        if (curso.isEmpty() || matricula.isEmpty() || nome.isEmpty() || senha.isEmpty()) {
            toast("Preencha todos os campos do Aluno.")
            return
        }

        if (!matricula.all { it.isDigit() }) {
            toast("A matrícula deve conter apenas números.")
            return
        }
        if (matricula.length != 7) {
            toast("A matrícula do aluno deve ter exatamente 7 dígitos.")
            return
        }

        if (!senha.all { it.isDigit() }) {
            toast("A senha deve conter apenas números.")
            return
        }
        if (senha.length != 8) {
            toast("A senha deve ter exatamente 8 dígitos.")
            return
        }

        // ===== NOVO: VERIFICAR SE A MATRÍCULA JÁ EXISTE =====
        db.collection("alunos").document(matricula).get()
            .addOnSuccessListener { doc ->
                if (doc.exists()) {
                    toast("Matrícula já existente.")
                    return@addOnSuccessListener
                } else {
                    salvarAluno(curso, matricula, nome, senha)
                }
            }
    }

    private fun salvarAluno(curso: String, matricula: String, nome: String, senha: String) {
        val dados = mapOf(
            "tipo" to "aluno",
            "curso" to curso,
            "matricula" to matricula,
            "nome" to nome,
            "senha" to senha,
            "fotoPerfil" to (fotoAlunoBase64 ?: "")
        )

        db.collection("alunos").document(matricula)
            .set(dados)
            .addOnSuccessListener {
                toast("Aluno cadastrado! ID = $matricula")
                irParaLogin(matricula)
            }
            .addOnFailureListener { e ->
                toast("Erro ao cadastrar: ${e.message}")
            }
    }

    // ============================================================
    // =====================   ADMINISTRADOR   ====================
    // ============================================================
    private fun cadastrarAdmin() {
        val cargo = findViewById<EditText>(R.id.inputCargoAdmin).text.toString()
        val matricula = findViewById<EditText>(R.id.inputMatriculaAdmin).text.toString()
        val nome = findViewById<EditText>(R.id.inputNomeAdmin).text.toString()
        val senha = findViewById<EditText>(R.id.inputSenhaAdmin).text.toString()

        if (cargo.isEmpty() || matricula.isEmpty() || nome.isEmpty() || senha.isEmpty()) {
            toast("Preencha todos os campos do Administrador.")
            return
        }

        if (!matricula.all { it.isDigit() }) {
            toast("A matrícula deve conter apenas números.")
            return
        }
        if (matricula.length != 8) {
            toast("A matrícula do administrador deve ter exatamente 8 dígitos.")
            return
        }

        if (!senha.all { it.isDigit() }) {
            toast("A senha deve conter apenas números.")
            return
        }
        if (senha.length != 8) {
            toast("A senha deve ter exatamente 8 dígitos.")
            return
        }

        // ===== NOVO: VERIFICAR SE A MATRÍCULA JÁ EXISTE =====
        db.collection("administrador").document(matricula).get()
            .addOnSuccessListener { doc ->
                if (doc.exists()) {
                    toast("Matrícula já existente.")
                    return@addOnSuccessListener
                } else {
                    salvarAdmin(cargo, matricula, nome, senha)
                }
            }
    }

    private fun salvarAdmin(cargo: String, matricula: String, nome: String, senha: String) {
        val dados = mapOf(
            "tipo" to "administrador",
            "cargo" to cargo,
            "matricula" to matricula,
            "nome" to nome,
            "senha" to senha,
            "fotoPerfil" to (fotoAdminBase64 ?: "")
        )

        db.collection("administrador").document(matricula)
            .set(dados)
            .addOnSuccessListener {
                toast("Administrador cadastrado! ID = $matricula")
                irParaLogin(matricula)
            }
            .addOnFailureListener { e ->
                toast("Erro ao cadastrar: ${e.message}")
            }
    }

    private fun irParaLogin(matricula: String) {
        val intent = Intent(this, LoginUsuarioActivity::class.java).apply {
            putExtra("matricula", matricula)
        }
        startActivity(intent)
        finish()
    }

    private fun destacarSelecionado(selecionado: Button, outro: Button) {
        selecionado.setBackgroundColor(android.graphics.Color.parseColor("#002A50"))
        selecionado.setTextColor(android.graphics.Color.WHITE)

        outro.setBackgroundColor(android.graphics.Color.parseColor("#004070"))
        outro.setTextColor(android.graphics.Color.WHITE)
    }

    private fun toast(msg: String) =
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}
