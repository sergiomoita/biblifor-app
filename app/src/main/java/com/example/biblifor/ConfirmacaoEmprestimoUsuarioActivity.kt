package com.example.biblifor

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.content.Intent
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ConfirmacaoEmprestimoUsuarioActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmacao_emprestimo_usuario)

        // ================ RECEBE DADOS DA TELA ANTERIOR ================
        val titulo = intent.getStringExtra("titulo") ?: ""
        val livroId = intent.getStringExtra("livroId") ?: ""
        val imagemBase64 = intent.getStringExtra("imagemBase64")
        val localizacaoRecebida = intent.getStringExtra("localizacao") ?: ""

        // ================ VIEWS ================
        val imgCapa = findViewById<ImageView>(R.id.imgCapaConfirmacao)
        val txtLocalizacao = findViewById<TextView>(R.id.txtLocalizacao)
        val btnHistorico = findViewById<Button>(R.id.btnIrHistorico)

        // Se a tela recebeu localização da Activity anterior, já mostra
        if (localizacaoRecebida.isNotEmpty()) {
            txtLocalizacao.text = localizacaoRecebida
        }

        // ================ CARREGAR IMAGEM BASE64 ================
        if (!imagemBase64.isNullOrBlank()) {
            try {
                val bytes = Base64.decode(imagemBase64, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                imgCapa.setImageBitmap(bitmap)
            } catch (e: Exception) {
                imgCapa.setImageResource(R.drawable.livro_rachelqueiroz)
            }
        } else {
            imgCapa.setImageResource(R.drawable.livro_rachelqueiroz)
        }

        // ================  PEGAR LOCALIZAÇÃO DO FIREBASE  ================
        if (livroId.isNotEmpty()) {
            Firebase.firestore.collection("livros")
                .document(livroId)
                .get()
                .addOnSuccessListener { doc ->
                    val localiz = doc.getString("CodigoAcervo") ?: ""
                    if (localiz.isNotEmpty()) {
                        txtLocalizacao.text = localiz
                    }
                }
        }


        // ================  BOTÃO → HISTÓRICO ================
        btnHistorico.setOnClickListener {
            startActivity(Intent(this, HistoricoEmprestimosUsuarioActivity::class.java))
        }

        // ================  BARRA SUPERIOR ================
        findViewById<ImageView>(R.id.iconChatbot).setOnClickListener {
            startActivity(Intent(this, ChatbotUsuarioActivity::class.java))
        }

        findViewById<ImageView>(R.id.iconNotificacao).setOnClickListener {
            startActivity(Intent(this, AvisosUsuarioActivity::class.java))
        }

        // ================ BARRA INFERIOR ================
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
    }
}
