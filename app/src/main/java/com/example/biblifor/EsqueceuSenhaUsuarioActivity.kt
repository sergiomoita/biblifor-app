package com.example.biblifor

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore

class EsqueceuSenhaUsuarioActivity : BaseActivity() {

    private lateinit var fb: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_esqueceu_senha_usuario)

        // Referência do Firestore (bem no estilo do código do Narak)
        fb = FirebaseFirestore.getInstance()

        val btnVoltar = findViewById<ImageView>(R.id.btnVoltarEsqueceuSenhaUsuarioSergio)
        val btnAcessar = findViewById<Button>(R.id.btnAcessarEsqueceuSenhaUsuarioSergio)
        val inputMatricula =
            findViewById<EditText>(R.id.inputMatriculaEsqueceuSenhaUsuarioSergio)
        val inputSenha =
            findViewById<EditText>(R.id.inputSenhaEsqueceuSenhaUsuarioSergio)

        btnVoltar.setOnClickListener {
            val intent = Intent(this, LoginUsuarioActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnAcessar.setOnClickListener {
            val matricula = inputMatricula.text.toString().trim()
            val novaSenha = inputSenha.text.toString().trim()

            if (matricula.isEmpty() || novaSenha.isEmpty()) {
                Toast.makeText(
                    this,
                    "Preencha matrícula e nova senha",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                recuperarSenha(matricula, novaSenha)
            }
        }
    }

    private fun recuperarSenha(matricula: String, novaSenha: String) {
        // 1) Tenta na coleção "alunos" usando a matrícula como ID do documento
        fb.collection("alunos")
            .document(matricula)
            .get()
            .addOnSuccessListener { docAluno ->
                if (docAluno.exists()) {
                    atualizarSenhaEmColecao("alunos", matricula, novaSenha)
                } else {
                    // 2) Se não achou aluno, tenta em "administrador"
                    fb.collection("administrador")
                        .document(matricula)
                        .get()
                        .addOnSuccessListener { docAdm ->
                            if (docAdm.exists()) {
                                atualizarSenhaEmColecao("administrador", matricula, novaSenha)
                            } else {
                                // Não achou em nenhuma coleção
                                mostrarToastMatriculaInvalida()
                            }
                        }
                        .addOnFailureListener {
                            Toast.makeText(
                                this,
                                "Erro ao buscar administrador no banco de dados",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }
            }
            .addOnFailureListener {
                Toast.makeText(
                    this,
                    "Erro ao buscar aluno no banco de dados",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun atualizarSenhaEmColecao(
        colecao: String,
        docId: String,
        novaSenha: String
    ) {
        fb.collection(colecao)
            .document(docId)
            .update("senha", novaSenha)
            .addOnSuccessListener {
                Toast.makeText(
                    this,
                    "Senha atualizada com sucesso!",
                    Toast.LENGTH_SHORT
                ).show()

                val intent = Intent(this, LoginUsuarioActivity::class.java)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(
                    this,
                    "Erro ao atualizar senha",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun mostrarToastMatriculaInvalida() {
        val inflater = LayoutInflater.from(this)
        val layout = inflater.inflate(R.layout.toast_matricula_invalida, null)

        val toast = Toast(applicationContext)
        toast.duration = Toast.LENGTH_SHORT
        toast.view = layout
        toast.setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, 100)
        toast.show()
    }
}
