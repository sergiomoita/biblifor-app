package com.example.biblifor

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RenovacaoEmprestimoUsuarioActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_renovacao_emprestimo_usuario)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }



        val imgSeta = findViewById<ImageView>(R.id.lopesSetaVoltar33)
        imgSeta.setOnClickListener {
            val navegarSeta = Intent(this, DisponiveisRenovacaoUsuarioActivity::class.java)
            startActivity(navegarSeta)
        }

        val btnAceitar = findViewById<Button>(R.id.lopesBtnAceitar33)
        btnAceitar.setOnClickListener {
            val navegarAceitar = Intent(this, RenovacaoConfirmadaUsuarioActivity::class.java)
            startActivity(navegarAceitar)
        }

        val btnRecusar = findViewById<Button>(R.id.lopesBtnRecusar33)
        btnRecusar.setOnClickListener {
            val navegarRecusar = Intent(this, DisponiveisRenovacaoUsuarioActivity::class.java)
            startActivity(navegarRecusar)
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
            val navegarLogoMenu3 = Intent(this, MenuHamburguerUsuarioActivity::class.java)
            startActivity(navegarLogoMenu3)
        }


        // ===== RecyclerView: termos e condições =====
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerTermos33)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val termos = listOf(
            Termo("1. Cadastro — Apenas usuários devidamente cadastrados na biblioteca podem realizar empréstimos."),
            Termo("2. Prazo de Empréstimo — Devolução em até 7 dias corridos; pode haver 1 renovação se não houver reserva."),
            Termo("3. Renovação — Solicitar antes do vencimento do prazo, presencialmente ou pelos meios oficiais."),
            Termo("4. Atrasos — Sujeito à suspensão de novos empréstimos até a regularização."),
            Termo("5. Penalidades — Podem ser aplicadas conforme regulamento interno da biblioteca."),
            Termo("6. Conservação — Materiais devem ser mantidos em bom estado; danos são de responsabilidade do usuário."),
            Termo("7. Devolução — Realizar no balcão ou canais autorizados pela biblioteca."),
            Termo("8. Reservas — Permitidas quando o título estiver emprestado."),
            Termo("9. Comunicação — Avisos podem ser enviados pelo app/e-mail.")
        )

        recyclerView.adapter = TermosAdapter(termos)
        // ============================================
    }
}
