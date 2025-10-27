package com.example.biblifor

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MenuPrincipalAdministradorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu_principal_administrador)

        // ===== Ajuste de insets (barras do sistema) =====
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // ===== PERFIL (foto -> abre perfil do administrador) =====
        val leoBotaoPerfilAdm = findViewById<ImageView>(R.id.leoFotoAdministrador37)
        leoBotaoPerfilAdm.setOnClickListener {
            startActivity(Intent(this, PerfilAdministradorActivity::class.java))
        }

        // ===== ACESSIBILIDADE =====
        val botaoAcessibilidade = findViewById<ImageView>(R.id.leoBotaoAcessibilidadeAdm37)
        val textosParaAcessibilidade = listOf<TextView>(
            findViewById(R.id.leotextViewUniforAdministrador37),
            findViewById(R.id.leoOlaAdministrador37),
            findViewById(R.id.leoMatriculaAdministrador37),
            findViewById(R.id.leoTituloAcervoAdm37),
            findViewById(R.id.leoTituloCapsulasAdm37),
            findViewById(R.id.leoTituloEventosAdm37),
            findViewById(R.id.leoNovaMensagemAdm37),
            findViewById(R.id.leoTituloEvento1Adm37),
            findViewById(R.id.leoCorpoEvento1Adm37),
            findViewById(R.id.leoTItuloEvento2Adm37),
            findViewById(R.id.leoCorpoTextoEvento2Adm37)
        )

        val coresOriginais = textosParaAcessibilidade.map { it to it.currentTextColor }
        val corAcessivel = Color.parseColor("#FFFF00") // Amarelo alto contraste

        var acessibilidadeAtiva = false
        botaoAcessibilidade.setOnClickListener {
            if (!acessibilidadeAtiva) {
                textosParaAcessibilidade.forEach { it.setTextColor(corAcessivel) }
            } else {
                coresOriginais.forEach { (view, color) -> view.setTextColor(color) }
            }
            acessibilidadeAtiva = !acessibilidadeAtiva
        }

        // ===== BOTÕES DE AÇÃO =====
        val botaoEscreverMensagem = findViewById<ImageView>(R.id.leoBotaoEscreverMensagemAdm37)
        botaoEscreverMensagem.setOnClickListener {
            startActivity(Intent(this, EscreverMensagemAdministradorActivity::class.java))
        }

        val botaoCadastrar = findViewById<Button>(R.id.leoBotaoCadastrarAdm37)
        botaoCadastrar.setOnClickListener {
            startActivity(Intent(this, CadastrarLivroAdministradorActivity::class.java))
        }

        val botaoEmprestar = findViewById<Button>(R.id.leoBotaoEmprestarAdm37)
        botaoEmprestar.setOnClickListener {
            startActivity(Intent(this, LivrosEmprestaveisAdministradorActivity::class.java))
        }

        val botaoVerMaisCapsulas = findViewById<TextView>(R.id.btnVerMaisCapsulas)
        botaoVerMaisCapsulas.setOnClickListener {
            startActivity(Intent(this, CapsulasAdministradorActivity::class.java))
        }

        // ===== SEÇÃO NOVA MENSAGEM (toda área clicável) =====
                val secaoNovaMensagem = findViewById<LinearLayout>(R.id.secaoNovaMensagem)
                val botaoNovaMensagem = findViewById<ImageView>(R.id.leoBotaoNovaMensagem237)

        // Clique no texto ou ícone leva para a mesma tela
                val abrirNovaMensagem = Intent(this, EscreverMensagemAdministradorActivity::class.java)

                secaoNovaMensagem.setOnClickListener {
                    startActivity(abrirNovaMensagem)
                }

                botaoNovaMensagem.setOnClickListener {
                    startActivity(abrirNovaMensagem)
                }


        // ===== SEÇÕES CLICÁVEIS =====
        val secaoCapsulas = findViewById<LinearLayout>(R.id.secaoCapsulas)
        val secaoEventos = findViewById<LinearLayout>(R.id.secaoEventos)
        val verMaisEventos = findViewById<TextView>(R.id.btnVerMaisEventos)

        secaoCapsulas.setOnClickListener {
            startActivity(Intent(this, CapsulasAdministradorActivity::class.java))
        }

        secaoEventos.setOnClickListener {
            startActivity(Intent(this, MensagensAdministradorActivity::class.java))
        }

        verMaisEventos.setOnClickListener {
            startActivity(Intent(this, MensagensAdministradorActivity::class.java))
        }

        // ===== BARRA INFERIOR FIXA =====
        val bottomHome = findViewById<ImageView>(R.id.leoLogoHome3)
        val bottomChatbot = findViewById<ImageView>(R.id.leoImagemChatbot3)
        val bottomEmail = findViewById<ImageView>(R.id.leoImagemNotificacoes3)
        val bottomMenu = findViewById<ImageView>(R.id.leoImagemMenu3)

        bottomHome.setOnClickListener {
            startActivity(Intent(this, MenuPrincipalAdministradorActivity::class.java))
            finish()
        }

        bottomChatbot.setOnClickListener {
            startActivity(Intent(this, EscreverMensagemAdministradorActivity::class.java))
        }

        bottomEmail.setOnClickListener {
            startActivity(Intent(this, MensagensAdministradorActivity::class.java))
        }

        bottomMenu.setOnClickListener {
            startActivity(Intent(this, MenuHamburguerAdministradorActivity::class.java))
        }
    }
}
