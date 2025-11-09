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

            // ✅ Agora autentica primeiro em "administrador" e depois em "alunos"
            autenticarUsuario(matricula, senha)
        }

        tvEsqueceuSenha.setOnClickListener {
            startActivity(Intent(this, EsqueceuSenhaUsuarioActivity::class.java))
        }

        tvCadastrar.setOnClickListener {
            startActivity(Intent(this, CadastroUsuarioActivity::class.java))
        }
    }

    /** Orquestra a autenticação:
     *  1) procura na coleção "administrador"
     *  2) se não achar, procura em "alunos"
     */
    private fun autenticarUsuario(matricula: String, senhaDigitada: String) {
        val admins = db.collection("administrador")

        // Tenta pelo ID do documento (docId == matrícula)
        admins.document(matricula).get()
            .addOnSuccessListener { doc ->
                if (doc.exists()) {
                    validarSenhaEDirecionar(
                        senhaNoBanco = doc.getString("senha"),
                        tipo = doc.getString("tipo") ?: "administrador",
                        nome = doc.getString("nome"),
                        senhaDigitada = senhaDigitada
                    )
                } else {
                    // Fallback: query por campo "matricula"
                    admins.whereEqualTo("matricula", matricula).limit(1).get()
                        .addOnSuccessListener { query ->
                            if (!query.isEmpty) {
                                val d = query.documents.first()
                                validarSenhaEDirecionar(
                                    senhaNoBanco = d.getString("senha"),
                                    tipo = d.getString("tipo") ?: "administrador",
                                    nome = d.getString("nome"),
                                    senhaDigitada = senhaDigitada
                                )
                            } else {
                                // Não é admin → tenta alunos
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

    /** Autenticação na coleção "alunos" (igual ao que já estava funcionando) */
    private fun autenticarAluno(matricula: String, senhaDigitada: String) {
        val alunos = db.collection("alunos")

        alunos.document(matricula).get()
            .addOnSuccessListener { doc ->
                if (doc.exists()) {
                    validarSenhaEDirecionar(
                        senhaNoBanco = doc.getString("senha"),
                        tipo = doc.getString("tipo") ?: "aluno",
                        nome = doc.getString("nome"),
                        senhaDigitada = senhaDigitada
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
                                senhaDigitada = senhaDigitada
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

    /** Decide a rota com base no tipo e confere senha */
    private fun validarSenhaEDirecionar(
        senhaNoBanco: String?,
        tipo: String?,
        nome: String?,
        senhaDigitada: String
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
        if (papel == "admin" || papel == "administrador") {
            startActivity(Intent(this, MenuPrincipalAdministradorActivity::class.java).apply {
                putExtra("NOME_USUARIO", nome ?: "")
            })
        } else {
            startActivity(Intent(this, MenuPrincipalUsuarioActivity::class.java).apply {
                putExtra("NOME_USUARIO", nome ?: "")
            })
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
