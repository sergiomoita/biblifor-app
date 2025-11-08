package com.example.biblifor

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PopupHistoricoEmprestimosUsuarioActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_popup_historico_emprestimos_usuario)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Botão Avaliar leva para AvaliacaoUsuarioActivity
        val botaoAvaliar = findViewById<Button>(R.id.ButtonAvaliarGustavo03)
        botaoAvaliar.setOnClickListener {
            val intent = Intent(this, AvaliacaoUsuarioActivity::class.java)
            startActivity(intent)
        }

        val botarSair = findViewById<ImageView>(R.id.imageViewXMenu)
        botarSair.setOnClickListener {
            val intent = Intent(this, PerfilUsuarioActivity::class.java)
            startActivity(intent)
        }
    }
}