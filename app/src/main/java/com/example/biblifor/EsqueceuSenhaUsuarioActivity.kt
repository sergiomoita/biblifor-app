package com.example.biblifor

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class EsqueceuSenhaUsuarioActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_esqueceu_senha_usuario)

        val btnVoltar = findViewById<ImageView>(R.id.btnVoltarEsqueceuSenhaUsuarioSergio)
        val btnAcessar = findViewById<Button>(R.id.btnAcessarEsqueceuSenhaUsuarioSergio)
        val inputMatricula = findViewById<EditText>(R.id.inputMatriculaEsqueceuSenhaUsuarioSergio)
        val inputSenha = findViewById<EditText>(R.id.inputSenhaEsqueceuSenhaUsuarioSergio)

        btnVoltar.setOnClickListener {
            val intent = Intent(this, LoginUsuarioActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnAcessar.setOnClickListener {
            val matricula = inputMatricula.text.toString().trim()

            if (matricula == "123") {
                val intent = Intent(this, MenuPrincipalUsuarioActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                mostrarToastMatriculaInvalida()
            }
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
