package com.example.biblifor

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class EmprestarLivroAdministradorActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emprestar_livro_administrador)
        window.statusBarColor = android.graphics.Color.TRANSPARENT

        val textoEmprestavel = findViewById<TextView>(R.id.lopesTextoEmprestavel45)
        val textoDescricao = findViewById<TextView>(R.id.lopesTornar45)
        val botaoSim = findViewById<Button>(R.id.lopesBtnSim45)
        val botaoNao = findViewById<Button>(R.id.lopesBtnNao45)
        val botaoMenu = findViewById<Button>(R.id.buttonMenu)
        val botaoEditar = findViewById<ImageView>(R.id.iconeEditarLivro)

        botaoSim.setOnClickListener {
            textoEmprestavel.text = "Emprestável"
            textoEmprestavel.setTextColor(Color.parseColor("#2E7D32")) // verde escuro
            textoDescricao.text = "Livro liberado para empréstimo."
        }

        botaoNao.setOnClickListener {
            textoEmprestavel.text = "Não emprestável"
            textoEmprestavel.setTextColor(Color.parseColor("#C62828")) // vermelho escuro
            textoDescricao.text = "Livro bloqueado para empréstimo."
        }

        botaoMenu.setOnClickListener {
            val intent = Intent(this, MenuPrincipalAdministradorActivity::class.java)
            startActivity(intent)
        }

        botaoEditar.setOnClickListener {
            val intent = Intent(this, EditarLivroAdministradorActivity::class.java)
            startActivity(intent)
        }
    }
}
