package com.example.biblifor

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LoginUsuarioActivity : BaseActivity() {

    private val db by lazy { Firebase.firestore }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_usuario)

        val btnAcessar = findViewById<Button>(R.id.btnAcessarLoginUsuarioSergio)
        val tvEsqueceuSenha = findViewById<TextView>(R.id.tvEsqueceuSenhaLoginUsuario)
        val etMatricula = findViewById<EditText>(R.id.inputMatriculaLoginUsuarioSergio)
        val etSenha = findViewById<EditText>(R.id.inputSenhaLoginUsuarioSergio)
        val tvCadastrar = findViewById<TextView>(R.id.tvCadastrarLoginUsuarioSergio)

        btnAcessar.setOnClickListener {
            val matricula = etMatricula.text.toString().trim()
            val senha = etSenha.text.toString().trim()

            if (matricula.isEmpty() || senha.isEmpty()) {
                mostrarToastErro("⚠️ Preencha todos os campos!")
                return@setOnClickListener
            }

            autenticarUsuario(matricula, senha)
        }

        tvEsqueceuSenha.setOnClickListener {
            startActivity(Intent(this, EsqueceuSenhaUsuarioActivity::class.java))
        }

        tvCadastrar.setOnClickListener {
            startActivity(Intent(this, CadastroUsuarioActivity::class.java))
        }
    }

    // 1) Tenta autenticar como administrador
    private fun autenticarUsuario(matricula: String, senhaDigitada: String) {
        val admins = db.collection("administrador")

        admins.document(matricula).get()
            .addOnSuccessListener { doc ->
                if (doc.exists()) {
                    validarSenhaEDirecionar(
                        senhaNoBanco = doc.getString("senha"),
                        tipo = doc.getString("tipo") ?: "administrador",
                        nome = doc.getString("nome"),
                        senhaDigitada = senhaDigitada,
                        matricula = matricula
                    )
                } else {
                    // fallback → busca por campo
                    admins.whereEqualTo("matricula", matricula).limit(1).get()
                        .addOnSuccessListener { query ->
                            if (!query.isEmpty) {
                                val d = query.documents.first()
                                validarSenhaEDirecionar(
                                    senhaNoBanco = d.getString("senha"),
                                    tipo = d.getString("tipo") ?: "administrador",
                                    nome = d.getString("nome"),
                                    senhaDigitada = senhaDigitada,
                                    matricula = matricula
                                )
                            } else {
                                autenticarAluno(matricula, senhaDigitada)
                            }
                        }
                        .addOnFailureListener { e ->
                            mostrarToastErro("Erro ao buscar administrador: ${e.localizedMessage}")
                        }
                }
            }
            .addOnFailureListener { e ->
                mostrarToastErro("Falha de conexão: ${e.localizedMessage}")
            }
    }

    // 2) Tenta autenticar aluno
    private fun autenticarAluno(matricula: String, senhaDigitada: String) {
        val alunos = db.collection("alunos")

        alunos.document(matricula).get()
            .addOnSuccessListener { doc ->
                if (doc.exists()) {
                    validarSenhaEDirecionar(
                        senhaNoBanco = doc.getString("senha"),
                        tipo = doc.getString("tipo") ?: "aluno",
                        nome = doc.getString("nome"),
                        senhaDigitada = senhaDigitada,
                        matricula = matricula
                    )
                } else {
                    alunos.whereEqualTo("matricula", matricula).limit(1).get()
                        .addOnSuccessListener { query ->
                            if (query.isEmpty) {
                                mostrarToastErro("❌ Usuário não encontrado!")
                                return@addOnSuccessListener
                            }
                            val d = query.documents.first()
                            validarSenhaEDirecionar(
                                senhaNoBanco = d.getString("senha"),
                                tipo = d.getString("tipo") ?: "aluno",
                                nome = d.getString("nome"),
                                senhaDigitada = senhaDigitada,
                                matricula = matricula
                            )
                        }
                        .addOnFailureListener { e ->
                            mostrarToastErro("Erro ao buscar aluno: ${e.localizedMessage}")
                        }
                }
            }
            .addOnFailureListener { e ->
                mostrarToastErro("Falha de conexão: ${e.localizedMessage}")
            }
    }

    /** Recebe credenciais e decide rota */
    private fun validarSenhaEDirecionar(
        senhaNoBanco: String?,
        tipo: String?,
        nome: String?,
        senhaDigitada: String,
        matricula: String
    ) {
        if (senhaNoBanco == null) {
            mostrarToastErro("❌ Registro sem senha. Contate o suporte.")
            return
        }
        if (senhaNoBanco != senhaDigitada) {
            mostrarToastErro("❌ Matrícula ou senha incorretos!")
            return
        }

        val papel = (tipo ?: "aluno").lowercase()

        val prefs = getSharedPreferences("APP_PREFS", MODE_PRIVATE)
        val editor = prefs.edit()

        if (papel == "admin" || papel == "administrador") {

            editor.putString("MATRICULA_ADM", matricula)   // ✅ salva ADM
            editor.apply()

            startActivity(
                Intent(this, MenuPrincipalAdministradorActivity::class.java).apply {
                    putExtra("NOME_USUARIO", nome ?: "")
                }
            )

        } else {

            editor.putString("MATRICULA_USER", matricula)  // ✅ salva USER
            editor.apply()

            startActivity(
                Intent(this, MenuPrincipalUsuarioActivity::class.java).apply {
                    putExtra("NOME_USUARIO", nome ?: "")
                }
            )
        }

        finish()
    }

    private fun mostrarToastErro(mensagem: String) {
        val inflater = LayoutInflater.from(this)
        val layout = inflater.inflate(R.layout.toast_erro_login, null)
        val tvMensagem = layout.findViewById<TextView>(R.id.textoToastErro)
        tvMensagem.text = mensagem
        Toast(applicationContext).apply {
            duration = Toast.LENGTH_SHORT
            view = layout
        }.show()
    }
}
