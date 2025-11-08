package com.example.biblifor

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.text.Normalizer

class MenuPrincipalUsuarioActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu_principal_usuario)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnHistorico = findViewById<LinearLayout>(R.id.btnHistorico)
        val btnFavoritos = findViewById<LinearLayout>(R.id.btnFavoritos)
        val btnAvisos = findViewById<LinearLayout>(R.id.btnAvisos)

        // Clique no título ou em "Ver mais"
        btnHistorico.setOnClickListener {
            startActivity(Intent(this, HistoricoEmprestimosUsuarioActivity::class.java))
        }

        btnFavoritos.setOnClickListener {
            startActivity(Intent(this, FavoritosUsuarioActivity::class.java))
        }

        btnAvisos.setOnClickListener {
            startActivity(Intent(this, AvisosUsuarioActivity::class.java))
        }


        // ===== PERFIL =====
        val imagemPerfil = findViewById<ImageView>(R.id.leoFotoUser3)
        imagemPerfil.setOnClickListener {
            startActivity(Intent(this, PerfilUsuarioActivity::class.java))
        }

        // ===== ACESSIBILIDADE: alternar cores de alto contraste =====
        val imagemAcessibilidade = findViewById<ImageView>(R.id.leoAcessibilidade3)
        val inputPesquisa = findViewById<EditText>(R.id.leoPesquisa3)

        val textosParaAcessibilidade = listOf<TextView>(
            findViewById(R.id.leotextViewUnifor3),
            findViewById(R.id.leoOlaUsuario3),
            findViewById(R.id.leoMatricula3),
            findViewById(R.id.leoTituloHistorico3),
            findViewById(R.id.leoLegendaEmaDeitada3),
            findViewById(R.id.leoFavoritos3),
            findViewById(R.id.leoUltimosAvisos3),
            findViewById(R.id.leoAviso3),
            findViewById(R.id.leoAviso4)
        )


        val coresOriginais = textosParaAcessibilidade.map { it to it.currentTextColor }
        val corOriginalInput = inputPesquisa.currentTextColor
        val corAcessivel = Color.parseColor("#FFFF00")

        var acessibilidadeAtiva = false
        imagemAcessibilidade.setOnClickListener {
            if (!acessibilidadeAtiva) {
                textosParaAcessibilidade.forEach { it.setTextColor(corAcessivel) }
                inputPesquisa.setTextColor(corAcessivel)
            } else {
                coresOriginais.forEach { (view, color) -> view.setTextColor(color) }
                inputPesquisa.setTextColor(corOriginalInput)
            }
            acessibilidadeAtiva = !acessibilidadeAtiva
        }

        // ===== NAVEGAÇÕES =====
        findViewById<ImageView>(R.id.leoNotificacao3).setOnClickListener {
            startActivity(Intent(this, AvisosUsuarioActivity::class.java))
        }

        findViewById<ImageView>(R.id.leoLogoHome3).setOnClickListener {
            startActivity(Intent(this, MenuPrincipalUsuarioActivity::class.java))
        }

        findViewById<ImageView>(R.id.leoImagemChatbot3).setOnClickListener {
            startActivity(Intent(this, ChatbotUsuarioActivity::class.java))
        }

        findViewById<ImageView>(R.id.leoImagemNotificacoes3).setOnClickListener {
            startActivity(Intent(this, AvisosUsuarioActivity::class.java))
        }

        findViewById<ImageView>(R.id.leoImagemMenu3).setOnClickListener {
            startActivity(Intent(this, MenuHamburguerUsuarioActivity::class.java))
        }

        // ===== PESQUISA (LUPA) =====
        val imagemLupa = findViewById<ImageView>(R.id.leoLupaPesquisa3)
        imagemLupa.setOnClickListener {
            val textoBruto = inputPesquisa.text?.toString()?.trim().orEmpty()
            val normalizado = Normalizer.normalize(textoBruto, Normalizer.Form.NFD)
                .replace(Regex("\\p{InCombiningDiacriticalMarks}+"), "")
                .lowercase()

            when (normalizado) {
                "o quinze" -> startActivity(Intent(this, ResultadosPesquisaUsuarioActivity::class.java))
                "laranjeira" -> startActivity(Intent(this, MensagemSemResultadoUsuarioActivity::class.java))
                else -> startActivity(Intent(this, ResultadosPesquisaUsuarioActivity::class.java))
            }
        }
    }
}
