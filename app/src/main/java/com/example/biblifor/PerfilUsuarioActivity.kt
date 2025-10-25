package com.example.biblifor

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PerfilUsuarioActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Altere para o nome real do seu layout de Activity
        setContentView(R.layout.activity_perfil_usuario)

        // 1) RecyclerView da tela (certifique-se de que o XML tem esse id)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerHistorico5)

        // 2) LayoutManager
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        // (Opcional) Linha divisória entre itens
        recyclerView.addItemDecoration(
            DividerItemDecoration(this, layoutManager.orientation)
        )

        // 3) Dados de exemplo (substitua pelo que vier da sua fonte real)
        val historico = listOf(
            HistoricoEmprestimo("Romeu e Julieta - W.Shakespeare  13/09/2025"),
            HistoricoEmprestimo("1984 - George Orwell             02/09/2025"),
            HistoricoEmprestimo("Dom Casmurro - Machado de Assis  13/09/2025"),
            HistoricoEmprestimo("Ilíada - Homero                  13/09/2025"),
            HistoricoEmprestimo("Guerra e Paz - Liev Tolstói      13/09/2025"),
            HistoricoEmprestimo("Pai Rico, Pai Pobre - Robert Kiyosaki  13/09/2025")
        )

        // 4) Adapter
        recyclerView.adapter = HistoricoEmprestimoAdapter(historico)

        val leoBotaoVoltarPU5 = findViewById<ImageView>(R.id.leoImagemSetaPU5)
        leoBotaoVoltarPU5.setOnClickListener {
            val navegarVoltarPU5 = Intent (this, MenuPrincipalUsuarioActivity::class.java)
            startActivity(navegarVoltarPU5)
        }
        val leoBotaoNotificacoesSPU5 = findViewById<ImageView>(R.id.leoImagemNotificacaoSuperiorPU5)
        leoBotaoNotificacoesSPU5.setOnClickListener {
            val navegarNotificacoesSPU5 = Intent (this, AvisosUsuarioActivity::class.java)
            startActivity(navegarNotificacoesSPU5)
        }
        val leoLogoHomePU5 = findViewById<ImageView>(R.id.leoLogoHomePU5)
        leoLogoHomePU5.setOnClickListener {
            val navegarLogoHomePU5 = Intent (this, MenuPrincipalUsuarioActivity::class.java)
            startActivity(navegarLogoHomePU5)
        }
        val leoLogoChatBotPU5 = findViewById<ImageView>(R.id.leoImagemChatbotPU5)
        leoLogoChatBotPU5.setOnClickListener {
            val navegarLogoChatBotPU5 = Intent (this, ChatbotUsuarioActivity::class.java)
            startActivity(navegarLogoChatBotPU5)
        }
        val leoLogoNotificacoesPU5 = findViewById<ImageView>(R.id.leoImagemNotificacoesPU5)
        leoLogoNotificacoesPU5.setOnClickListener {
            val navegarLogoNotificacoesPU5 = Intent (this, AvisosUsuarioActivity::class.java)
            startActivity(navegarLogoNotificacoesPU5)
        }
        val leoLogoMenuPU5 = findViewById<ImageView>(R.id.leoImagemMenuPU5)
        leoLogoMenuPU5.setOnClickListener {
            val navegarLogoMenuPU5 = Intent (this, MenuPrincipalUsuarioActivity::class.java)
            startActivity(navegarLogoMenuPU5)
        }
    }
}