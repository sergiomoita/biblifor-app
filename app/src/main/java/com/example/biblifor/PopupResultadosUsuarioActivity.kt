package com.example.biblifor

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.biblifor.R

class PopupResultadosUsuarioActivity : AppCompatActivity() {

    private var isFavorito = false // Controla o estado do botão de favorito

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_popup_resultados_usuario)

        // --- Botão Voltar ---
        val btnVoltar = findViewById<ImageView>(R.id.btnVoltarPopupResultadosUsuarioSergio)
        btnVoltar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // --- Ícone de Favorito ---
        val iconFavorito = findViewById<ImageView>(R.id.iconFavoritoPopupResultadosUsuario)
        iconFavorito.setOnClickListener {
            isFavorito = !isFavorito // Alterna o estado (favoritado/desfavoritado)

            if (isFavorito) {
                iconFavorito.setImageResource(R.drawable.favoritado)
                Toast.makeText(this, "Adicionado aos favoritos", Toast.LENGTH_SHORT).show()
            } else {
                iconFavorito.setImageResource(R.drawable.desfavoritado)
                Toast.makeText(this, "Removido dos favoritos", Toast.LENGTH_SHORT).show()
            }
        }

        // --- Botões principais ---
        val btnEmprestimo = findViewById<Button>(R.id.btnEmprestimoPopupResultadosUsuario)
        val btnOnline = findViewById<Button>(R.id.btnOnlinePopupResultadosUsuario)
        val btnFavoritos = findViewById<Button>(R.id.btnFavoritosPopupResultadosUsuario)

        btnEmprestimo.setOnClickListener {
            val intent = Intent(this, EmprestimoUsuarioActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnOnline.setOnClickListener {
            Toast.makeText(this, "Abrindo versão online...", Toast.LENGTH_SHORT).show()
        }

        btnFavoritos.setOnClickListener {
            val intent = Intent(this, FavoritosUsuarioActivity::class.java)
            startActivity(intent)
            finish()
        }

        // ==============================
        // ⚙️ Funções da Barra Inferior
        // ==============================

        // 🏠 Home → MenuPrincipalUsuarioActivity
        val iconHome = findViewById<ImageView>(R.id.iconHomePopupResultadosUsuario)
        iconHome.setOnClickListener {
            val intent = Intent(this, MenuPrincipalUsuarioActivity::class.java)
            startActivity(intent)
            finish()
        }

        // 🤖 Chatbot inferior → ChatbotUsuarioActivity
        val iconMascoteInferior = findViewById<ImageView>(R.id.iconChatBotPopupResultadosUsuario)
        iconMascoteInferior.setOnClickListener {
            val intent = Intent(this, ChatbotUsuarioActivity::class.java)
            startActivity(intent)
        }

        // 💬 Mensagem inferior → AvisosUsuarioActivity
        val iconMensagem = findViewById<ImageView>(R.id.iconMensagemPopupResultadosUsuario)
        iconMensagem.setOnClickListener {
            val intent = Intent(this, AvisosUsuarioActivity::class.java)
            startActivity(intent)
        }

        // 🍔 Menu inferior → MenuPrincipalUsuarioActivity
        val iconMenu = findViewById<ImageView>(R.id.iconMenuPopupResultadosUsuario)
        iconMenu.setOnClickListener {
            val intent = Intent(this, MenuHamburguerUsuarioActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
