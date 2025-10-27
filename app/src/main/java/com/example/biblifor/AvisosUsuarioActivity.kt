package com.example.biblifor

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.biblifor.adapter.AvisoAdapter
import com.example.biblifor.model.Aviso

class AvisosUsuarioActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_avisos_usuario)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // --- RecyclerView ---
        val rv = findViewById<RecyclerView>(R.id.rvAvisosUsuario)
        rv.layoutManager = LinearLayoutManager(this)

        // Lista de avisos (com vários exemplos)
        val avisos = listOf(
            Aviso(
                "Unifor - Biblioteca",
                "12/09/2025",
                "Olá Maria Luísa. Este é um lembrete para devolver o livro \"Romeu e Julieta - William Shakespeare\" até 13/09/2025. Evite multas e permita que outros leitores também aproveitem essa obra."
            ),
            Aviso(
                "Unifor - Biblioteca",
                "12/08/2025",
                "Seu empréstimo foi confirmado. Você retirou \"Romeu e Julieta - William Shakespeare\". Devolução prevista para 13/09/2025. Boa leitura!"
            ),
            Aviso(
                "Unifor - Biblioteca",
                "12/07/2025",
                "Este é um lembrete para devolver o livro \"Dom Casmurro - Machado de Assis\" até 13/07/2025. Evite multas e permita que outros leitores também aproveitem essa obra."
            ),
            Aviso(
                "Unifor - Biblioteca",
                "12/06/2025",
                "Seu empréstimo foi confirmado. Você retirou \"Dom Casmurro - Machado de Assis\". Devolução prevista para 13/07/2025. Boa leitura!"
            ),
            Aviso(
                "Unifor - Biblioteca",
                "12/05/2025",
                "Este é um lembrete para devolver o livro \"Guerra e Paz - Liev Tolstói\" até 13/05/2025. Evite multas e permita que outros leitores também aproveitem essa obra."
            ),
            Aviso(
                "Unifor - Biblioteca",
                "12/04/2025",
                "Seu empréstimo foi confirmado. Você retirou \"Guerra e Paz - Liev Tolstói\". Devolução prevista para 13/07/2025. Boa leitura!"
            ),
            Aviso(
                "Unifor - Biblioteca",
                "12/03/2025",
                "Este é um lembrete para devolver o livro \"Pai Rico, Pai Pobre\" até 13/03/2025. Evite multas e permita que outros leitores também aproveitem essa obra."
            ),
            Aviso(
                "Unifor - Biblioteca",
                "12/02/2025",
                "Seu empréstimo foi confirmado. Você retirou \"Pai Rico, Pai Pobre\". Devolução prevista para 13/03/2025. Boa leitura!"
            ),
            Aviso(
                "Unifor - Biblioteca",
                "12/01/2025",
                "Seu empréstimo do livro \"O Pequeno Príncipe - Antoine de Saint-Exupéry\" foi cancelado. Esperamos vê-la em breve novamente!"
            ),
            Aviso(
                "Unifor - Biblioteca",
                "11/12/2024",
                "Aviso importante: o sistema passará por manutenção entre 13 e 15 de dezembro. Durante esse período, não será possível realizar empréstimos ou devoluções online."
            )
        )

        rv.adapter = AvisoAdapter(avisos)

        // --- Navegação ---
        val leoLogoHomeBFChatbot7 = findViewById<ImageView>(R.id.leoLogoHomeChatbotBF7)
        leoLogoHomeBFChatbot7.setOnClickListener {
            val navegarHomeChat7 = Intent(this, MenuPrincipalUsuarioActivity::class.java)
            startActivity(navegarHomeChat7)
        }

        val leoLogoChatbotBFChatbot7 = findViewById<ImageView>(R.id.leoImagemChatbotBF7)
        leoLogoChatbotBFChatbot7.setOnClickListener {
            val navegarChatbotChat7 = Intent(this, ChatbotUsuarioActivity::class.java)
            startActivity(navegarChatbotChat7)
        }

        val leoLogoNotificacoesBFChatbot7 = findViewById<ImageView>(R.id.leoImagemNotificacoesChatbotBF7)
        leoLogoNotificacoesBFChatbot7.setOnClickListener {
            val navegarNotificacoesChat7 = Intent(this, AvisosUsuarioActivity::class.java)
            startActivity(navegarNotificacoesChat7)
        }

        val leoLogoMenuBFChatbot7 = findViewById<ImageView>(R.id.leoImagemMenuChatbotBF7)
        leoLogoMenuBFChatbot7.setOnClickListener {
            val navegarMenuChat7 = Intent(this, MenuPrincipalUsuarioActivity::class.java)
            startActivity(navegarMenuChat7)
        }
    }
}
