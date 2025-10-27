package com.example.biblifor

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MenuHamburguerAdministradorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu_hamburguer_administrador)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val leoBotaoPerfilMenu49 = findViewById<TextView>(R.id.leoTituloPerfilMenu49)
        leoBotaoPerfilMenu49.setOnClickListener {
            val navegarPerfil49 = Intent (this, PerfilAdministradorActivity::class.java)
            startActivity(navegarPerfil49)
        }
        val leoBotaoCadastrarMenu49 = findViewById<TextView>(R.id.leoTituloCadastrarMenu49)
        leoBotaoCadastrarMenu49.setOnClickListener {
            val navegarCadastrar49 = Intent (this, CadastrarLivroAdministradorActivity::class.java)
            startActivity(navegarCadastrar49)
        }
        val leoBotaoEmprestarMenu49 = findViewById<TextView>(R.id.leoTituloEmprestarMenu49)
        leoBotaoEmprestarMenu49.setOnClickListener {
            val navegarEmprestar49 = Intent (this, LivrosEmprestaveisAdministradorActivity::class.java)
            startActivity(navegarEmprestar49)
        }
        val leoBotaoCapsulasMenu49 = findViewById<TextView>(R.id.leoTituloCapsulasMenu49)
        leoBotaoCapsulasMenu49.setOnClickListener {
            val navegarCapsulas49 = Intent (this, CapsulasAdministradorActivity::class.java)
            startActivity(navegarCapsulas49)
        }
        val leoBotaoEventosMenu49 = findViewById<TextView>(R.id.leoTituloEventosMenu49)
        leoBotaoEventosMenu49.setOnClickListener {
            val navegarEventos49 = Intent (this, MensagensAdministradorActivity::class.java)
            startActivity(navegarEventos49)
        }
        val leoBotaoMenuSuperior49 = findViewById<ImageView>(R.id.leoImagemMenuSuperior49)
        leoBotaoMenuSuperior49.setOnClickListener {
            val navegarMenuSuperior49 = Intent (this, MenuPrincipalAdministradorActivity::class.java)
            startActivity(navegarMenuSuperior49)
        }
    }
}