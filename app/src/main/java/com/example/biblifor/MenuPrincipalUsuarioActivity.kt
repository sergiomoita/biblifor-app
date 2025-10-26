package com.example.biblifor

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.text.Normalizer

class MenuPrincipalUsuarioActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu_principal_usuario)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val imagemPerfil3 = findViewById<ImageView>(R.id.leoFotoUser3)
        imagemPerfil3.setOnClickListener {
            val navegarPerfilMenu3 = Intent(this, PerfilUsuarioActivity::class.java)
            startActivity(navegarPerfilMenu3)
        }

        // ===== ACESSIBILIDADE: toggle de cor dos textos =====
        val imagemAcessibilidade3 = findViewById<ImageView>(R.id.leoAcessibilidade3)

        // TextViews afetados
        val textosParaAcessibilidade = listOf<TextView>(
            findViewById(R.id.leotextViewUnifor3),
            findViewById(R.id.leoOlaUsuario3),
            findViewById(R.id.leoMatricula3),
            findViewById(R.id.leoTituloHistorico3),
            findViewById(R.id.leoVerMais3),
            findViewById(R.id.leoLegendaEmaDeitada3),
            findViewById(R.id.leoFavoritos3),
            findViewById(R.id.leoVerMais23),
            findViewById(R.id.leoTituloLivroFavorito13),
            findViewById(R.id.leoTituloLivroFavorito23),
            findViewById(R.id.leoDisponivel3),
            findViewById(R.id.leoDisponivel23),
            findViewById(R.id.leoUltimosAvisos3),
            findViewById(R.id.leoVerMais33),
            findViewById(R.id.leoTituloAvisos3),
            findViewById(R.id.leoDataAviso3),
            findViewById(R.id.leoAviso3),
            findViewById(R.id.leoTituloAvisos23),
            findViewById(R.id.leoDataAviso23),
            findViewById(R.id.leoAviso23)
        )
        val inputPesquisa = findViewById<EditText>(R.id.leoPesquisa3)

        // Guardar cores originais
        val coresOriginais = textosParaAcessibilidade.map { it to it.currentTextColor }
        val corOriginalInput = inputPesquisa.currentTextColor

        // Cor de alto contraste
        val corAcessivel = Color.parseColor("#FFFF00")

        var acessibilidadeAtiva = false
        imagemAcessibilidade3.setOnClickListener {
            if (!acessibilidadeAtiva) {
                textosParaAcessibilidade.forEach { it.setTextColor(corAcessivel) }
                inputPesquisa.setTextColor(corAcessivel)
            } else {
                coresOriginais.forEach { (view, color) -> view.setTextColor(color) }
                inputPesquisa.setTextColor(corOriginalInput)
            }
            acessibilidadeAtiva = !acessibilidadeAtiva
        }
        // =====================================================

        val imagemNotificacao3 = findViewById<ImageView>(R.id.leoNotificacao3)
        imagemNotificacao3.setOnClickListener {
            val navegarNotificacao3 = Intent(this, AvisosUsuarioActivity::class.java)
            startActivity(navegarNotificacao3)
        }

        // ===== CONDICIONAL DA LUPA (usa o input leoPesquisa3) =====
        val imagemLupa3 = findViewById<ImageView>(R.id.leoLupaPesquisa3)
        imagemLupa3.setOnClickListener {
            val textoBruto = inputPesquisa.text?.toString()?.trim().orEmpty()

            val normalizado = Normalizer.normalize(textoBruto, Normalizer.Form.NFD)
                .replace(Regex("\\p{InCombiningDiacriticalMarks}+"), "")
                .lowercase()

            when (normalizado) {
                "o quinze" -> {
                    startActivity(Intent(this, ResultadosPesquisaUsuarioActivity::class.java))
                }
                "laranjeira" -> {
                    startActivity(Intent(this, PesquisaSemResultadoUsuarioActivity::class.java))
                }
                else -> {
                    startActivity(Intent(this, ResultadosPesquisaUsuarioActivity::class.java))
                }
            }
        }
        // ==========================================================

        val textVerMais3 = findViewById<TextView>(R.id.leoVerMais3)
        textVerMais3.setOnClickListener {
            val navegarVerMais3 = Intent(this, HistoricoEmprestimosUsuarioActivity::class.java)
            startActivity(navegarVerMais3)
        }

        val textVerMais23 = findViewById<TextView>(R.id.leoVerMais23)
        textVerMais23.setOnClickListener {
            val navegarVerMais23 = Intent(this, FavoritosUsuarioActivity::class.java)
            startActivity(navegarVerMais23)
        }

        val textVerMais33 = findViewById<TextView>(R.id.leoVerMais33)
        textVerMais33.setOnClickListener {
            val navegarVerMais33 = Intent(this, AvisosUsuarioActivity::class.java)
            startActivity(navegarVerMais33)
        }

        val imagemLogoHome3 = findViewById<ImageView>(R.id.leoLogoHome3)
        imagemLogoHome3.setOnClickListener {
            val navegarLogoHome3 = Intent(this, MenuPrincipalUsuarioActivity::class.java)
            startActivity(navegarLogoHome3)
        }

        val imagemLogoChatBot3 = findViewById<ImageView>(R.id.leoImagemChatbot3)
        imagemLogoChatBot3.setOnClickListener {
            val navegarLogoChatBot3 = Intent(this, ChatbotUsuarioActivity::class.java)
            startActivity(navegarLogoChatBot3)
        }

        val imagemLogoNotificacoes3 = findViewById<ImageView>(R.id.leoImagemNotificacoes3)
        imagemLogoNotificacoes3.setOnClickListener {
            val navegarLogoNotificacoes3 = Intent(this, AvisosUsuarioActivity::class.java)
            startActivity(navegarLogoNotificacoes3)
        }

        val imagemLogoMenu3 = findViewById<ImageView>(R.id.leoImagemMenu3)
        imagemLogoMenu3.setOnClickListener {
            val navegarLogoMenu3 = Intent(this, MenuHamburguerUsuario::class.java)
            startActivity(navegarLogoMenu3)
        }
    }
}
