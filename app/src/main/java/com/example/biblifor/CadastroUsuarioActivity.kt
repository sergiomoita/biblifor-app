package com.example.biblifor

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

//esse é um teste pra mostrar o git

class CadastroUsuarioActivity : AppCompatActivity() {

    private enum class Tipo { ALUNO, ADMIN }

    private lateinit var btnAluno: Button
    private lateinit var btnAdmin: Button
    private lateinit var groupAluno: LinearLayout
    private lateinit var groupAdmin: LinearLayout
    private lateinit var btnCadastrar: Button

    private var tipoSelecionado: Tipo? = null
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_usuario)

        btnAluno = findViewById(R.id.btnAluno)
        btnAdmin = findViewById(R.id.btnAdministrador)
        groupAluno = findViewById(R.id.groupAluno)
        groupAdmin = findViewById(R.id.groupAdministrador)
        btnCadastrar = findViewById(R.id.btnAcessarLoginUsuarioSergio)

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

        btnCadastrar.setOnClickListener {
            if (tipoSelecionado == null) {
                toast("Selecione Aluno ou Administrador.")
                return@setOnClickListener
            }

            when (tipoSelecionado) {
                Tipo.ALUNO -> cadastrarAluno()
                Tipo.ADMIN -> cadastrarAdmin()
                else -> Unit
            }
        }
    }

    // ====== ALUNO ======
    private fun cadastrarAluno() {
        val curso = findViewById<EditText>(R.id.inputCursoAluno).text.toString().trim()
        val matricula = findViewById<EditText>(R.id.inputMatriculaAluno).text.toString().trim()
        val nome = findViewById<EditText>(R.id.inputNomeAluno).text.toString().trim()
        val senha = findViewById<EditText>(R.id.inputSenhaAluno).text.toString().trim()

        if (curso.isEmpty() || matricula.isEmpty() || nome.isEmpty() || senha.isEmpty()) {
            toast("Preencha todos os campos do Aluno.")
            return
        }

        val dados = mapOf(
            "tipo" to "aluno",
            "curso" to curso,
            "matricula" to matricula,
            "nome" to nome,
            "senha" to senha
        )

        // Cria documento com ID = matrícula (ou atualiza se já existir)
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

    // ====== ADMIN ======
    private fun cadastrarAdmin() {
        val cargo = findViewById<EditText>(R.id.inputCargoAdmin).text.toString().trim()
        val matricula = findViewById<EditText>(R.id.inputMatriculaAdmin).text.toString().trim()
        val nome = findViewById<EditText>(R.id.inputNomeAdmin).text.toString().trim()
        val senha = findViewById<EditText>(R.id.inputSenhaAdmin).text.toString().trim()

        if (cargo.isEmpty() || matricula.isEmpty() || nome.isEmpty() || senha.isEmpty()) {
            toast("Preencha todos os campos do Administrador.")
            return
        }

        val dados = mapOf(
            "tipo" to "administrador",
            "cargo" to cargo,
            "matricula" to matricula,
            "nome" to nome,
            "senha" to senha
        )

        // Cria documento com ID = matrícula (ou atualiza se já existir)
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
        selecionado.backgroundTintList =
            android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor("#002A50"))
        selecionado.setTextColor(android.graphics.Color.WHITE)

        outro.backgroundTintList =
            android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor("#004070"))
        outro.setTextColor(android.graphics.Color.WHITE)
    }

    private fun toast(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}
