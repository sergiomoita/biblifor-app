package com.example.biblifor

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
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
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val leoBotaoPerfilAdm = findViewById<ImageView>(R.id.leoFotoAdministrador37)
        leoBotaoPerfilAdm.setOnClickListener {
            startActivity(Intent(this, PerfilAdministradorActivity::class.java))
        }

        // ===== ACESSIBILIDADE: toggle de cor dos textos =====
        val leoBotaoDeAcessibilidadeAdm = findViewById<ImageView>(R.id.leoBotaoAcessibilidadeAdm37)
        val textosParaAcessibilidade = listOf<TextView>(
            findViewById(R.id.leotextViewUniforAdministrador37),
            findViewById(R.id.leoOlaAdministrador37),
            findViewById(R.id.leoMatriculaAdministrador37),
            findViewById(R.id.leoTituloAcervoAdm37),
            findViewById(R.id.leoTituloCapsulasAdm37),
            findViewById(R.id.leoTituloEventosAdm37),
            findViewById(R.id.leoVerMaisAdministrador37),
            findViewById(R.id.leoNovaMensagemAdm37),
            findViewById(R.id.leoTituloEvento1Adm37),
            findViewById(R.id.leoDataEvento1Adm37),
            findViewById(R.id.leoCorpoEvento1Adm37),
            findViewById(R.id.leoTItuloEvento2Adm37), // mantém o id exatamente como está
            findViewById(R.id.leoDataEvento2Adm37),
            findViewById(R.id.leoCorpoTextoEvento2Adm37)
        )

        val coresOriginais = textosParaAcessibilidade.map { it to it.currentTextColor }
        val corAcessivel = Color.parseColor("#FFFF00")

        var acessibilidadeAtiva = false
        leoBotaoDeAcessibilidadeAdm.setOnClickListener {
            if (!acessibilidadeAtiva) {
                textosParaAcessibilidade.forEach { it.setTextColor(corAcessivel) }
            } else {
                coresOriginais.forEach { (view, color) -> view.setTextColor(color) }
            }
            acessibilidadeAtiva = !acessibilidadeAtiva
        }
        // =====================================================

        val leoBotaoEscreverMensagemAdm = findViewById<ImageView>(R.id.leoBotaoEscreverMensagemAdm37)
        leoBotaoEscreverMensagemAdm.setOnClickListener {
            startActivity(Intent(this, EscreverMensagemAdministradorActivity::class.java))
        }

        val leoBotaoCadastrarAdm = findViewById<Button>(R.id.leoBotaoCadastrarAdm37)
        leoBotaoCadastrarAdm.setOnClickListener {
            startActivity(Intent(this, CadastrarLivroAdministradorActivity::class.java))
        }

        val leoBotaoEmprestarAdm = findViewById<Button>(R.id.leoBotaoEmprestarAdm37)
        leoBotaoEmprestarAdm.setOnClickListener {
            startActivity(Intent(this, LivrosEmprestaveisAdministradorActivity::class.java))
        }

        val leoBotaoVerMaisCapsulasAdm = findViewById<TextView>(R.id.leoVerMaisAdministrador37)
        leoBotaoVerMaisCapsulasAdm.setOnClickListener {
            startActivity(Intent(this, CapsulasAdministradorActivity::class.java))
        }

        val leoBotaoEscreverMensagem2Adm = findViewById<ImageView>(R.id.leoBotaoNovaMensagem237)
        leoBotaoEscreverMensagem2Adm.setOnClickListener {
            startActivity(Intent(this, EscreverMensagemAdministradorActivity::class.java))
        }


        // ⚙️ Barra inferior
        findViewById<ImageView>(R.id.iconHomeCapsulasAdmSergio).setOnClickListener {
            startActivity(Intent(this, MenuPrincipalAdministradorActivity::class.java)); finish()
        }
        findViewById<ImageView>(R.id.iconEscreverMsgCapsulasAdmSergio).setOnClickListener {
            startActivity(Intent(this, EscreverMensagemAdministradorActivity::class.java))
        }
        findViewById<ImageView>(R.id.iconMensagemCapsulasAdmSergio).setOnClickListener {
            startActivity(Intent(this, MensagensAdministradorActivity::class.java))
        }
        findViewById<ImageView>(R.id.iconMenuInferiorCapsulasAdmSergio).setOnClickListener {
            startActivity(Intent(this, MenuPrincipalAdministradorActivity::class.java)); finish()
        }

    }
}
