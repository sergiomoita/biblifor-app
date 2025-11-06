package com.example.biblifor

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class LoginUsuarioActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_usuario)

        val btnAcessar = findViewById<Button>(R.id.btnAcessarLoginUsuarioSergio)
        val tvEsqueceuSenha = findViewById<TextView>(R.id.tvEsqueceuSenhaLoginUsuario)
        val etMatricula = findViewById<EditText>(R.id.inputMatriculaLoginUsuarioSergio)
        val etSenha = findViewById<EditText>(R.id.inputSenhaLoginUsuarioSergio)
        val tvCadastrar = findViewById<TextView>(R.id.tvCadastrarLoginUsuarioSergio) // 🔹 Novo texto "Cadastrar"

        btnAcessar.setOnClickListener {
            val matricula = etMatricula.text.toString().trim()
            val senha = etSenha.text.toString().trim()

            if (matricula.isEmpty() || senha.isEmpty()) {
                mostrarToastErro("⚠️ Preencha todos os campos!")
                return@setOnClickListener
            }

            when {
                matricula == "123" && senha == "usuario" -> {
                    startActivity(Intent(this, MenuPrincipalUsuarioActivity::class.java))
                    finish()
                }
                matricula == "333" && senha == "admin" -> {
                    startActivity(Intent(this, MenuPrincipalAdministradorActivity::class.java))
                    finish()
                }
                else -> {
                    mostrarToastErro("❌ Matrícula ou senha incorretos!")
                }
            }
        }

        tvEsqueceuSenha.setOnClickListener {
            startActivity(Intent(this, EsqueceuSenhaUsuarioActivity::class.java))
        }

        // 🔹 Quando clicar em "Cadastrar", vai para a tela de cadastro
        tvCadastrar.setOnClickListener {
            val intent = Intent(this, CadastroUsuarioActivity::class.java)
            startActivity(intent)
        }
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
