package com.example.biblifor

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MenuHamburguerUsuarioActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu_hamburguer_usuario)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val leoBotaoPerfilMenu25 = findViewById<TextView>(R.id.leoTituloPerfilMenu25)
        leoBotaoPerfilMenu25.setOnClickListener {
            val navegarPerfil25 = Intent (this, PerfilUsuarioActivity::class.java)
            startActivity(navegarPerfil25)
        }
        val leoBotaoAcervoMenu25 = findViewById<TextView>(R.id.leoTituloAcervoMenu25)
        leoBotaoAcervoMenu25.setOnClickListener {
            val navegarAcervo25 = Intent (this, AcervoUsuarioActivity::class.java)
            startActivity(navegarAcervo25)
        }
        val leoBotaoCapsulasMenu25 = findViewById<TextView>(R.id.leoTituloCapsulasMenu25)
        leoBotaoCapsulasMenu25.setOnClickListener {
            val navegarCapsulas25 = Intent (this, CapsulasUsuarioActivity::class.java)
            startActivity(navegarCapsulas25)
        }
        val leoBotaoHistoricoEMenu25 = findViewById<TextView>(R.id.leoTituloHistoricoEmprestimosMenu25)
        leoBotaoHistoricoEMenu25.setOnClickListener {
            val navegarHistoricoE25 = Intent (this, HistoricoEmprestimosUsuarioActivity::class.java)
            startActivity(navegarHistoricoE25)
        }
        val leoBotaoDisponiveisMenu25 = findViewById<TextView>(R.id.leoTituloDisponiveisMenu25)
        leoBotaoDisponiveisMenu25.setOnClickListener {
            val navegarDisponiveis25 = Intent (this, DisponiveisRenovacaoUsuarioActivity::class.java)
            startActivity(navegarDisponiveis25)
        }
        val leoBotaoRecomendadosMenu25 = findViewById<TextView>(R.id.leoTituloRecomendadosMenu25)
        leoBotaoRecomendadosMenu25.setOnClickListener {
            val navegarRecomendados25 = Intent (this, RecomendadosUsuarioActivity::class.java)
            startActivity(navegarRecomendados25)
        }
        val leoBotaoAvisosMenu25 = findViewById<TextView>(R.id.leoTituloAvisosMenu25)
        leoBotaoAvisosMenu25.setOnClickListener {
            val navegarAvisos25 = Intent (this, AvisosUsuarioActivity::class.java)
            startActivity(navegarAvisos25)
        }
        val leoBotaoPerguntasFMenu25 = findViewById<TextView>(R.id.leoTituloPerguntasFrequentesMenu25)
        leoBotaoPerguntasFMenu25.setOnClickListener {
            val navegarPerguntasF25 = Intent (this, PerguntasFrequentesUsuarioActivity::class.java)
            startActivity(navegarPerguntasF25)
        }
        val leoBotaoFavoritosMenu25 = findViewById<TextView>(R.id.leoTituloFavoritosMenu25)
        leoBotaoFavoritosMenu25.setOnClickListener {
            val navegarFavoritos25 = Intent (this, FavoritosUsuarioActivity::class.java)
            startActivity(navegarFavoritos25)
        }
        val leoMenuSuperior25 = findViewById<ImageView>(R.id.leoImagemMenuSuperior25)
        leoMenuSuperior25.setOnClickListener {
            finish()
        }
    }
}