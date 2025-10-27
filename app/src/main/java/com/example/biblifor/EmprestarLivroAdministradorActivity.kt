package com.example.biblifor

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class EmprestarLivroAdministradorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emprestar_livro_administrador)
        window.statusBarColor = android.graphics.Color.TRANSPARENT

        val textoEmprestavel = findViewById<TextView>(R.id.lopesTextoEmprestavel45)
        val textoDescricao = findViewById<TextView>(R.id.lopesTornar45)
        val botaoSim = findViewById<Button>(R.id.lopesBtnSim45)
        val botaoMenu = findViewById<Button>(R.id.buttonMenu)
        val botaoEditar = findViewById<ImageView>(R.id.iconeEditarLivro)

        botaoSim.setOnClickListener {
            textoEmprestavel.text = "Emprestável"
            textoEmprestavel.setTextColor(Color.parseColor("#2E7D32")) // verde escuro bonito
            textoDescricao.text = "Livro liberado para empréstimo."
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
