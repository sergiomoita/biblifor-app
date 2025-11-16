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
import com.google.firebase.firestore.FirebaseFirestore
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

        //matricula
        val matriculaRecebida = intent.getStringExtra("matricula")

        //textview
        val txtNome = findViewById<TextView>(R.id.leoOlaUsuario3)
        val txtMatricula = findViewById<TextView>(R.id.leoMatricula3)

        //buscar dados firebase
        if (matriculaRecebida != null) {
            val db = FirebaseFirestore.getInstance()

            db.collection("alunos")
                .document(matriculaRecebida)
                .get()
                .addOnSuccessListener { doc ->
                    if (doc.exists()) {
                        val nome = doc.getString("nome") ?: "Usuário"
                        val matricula = doc.getString("matricula") ?: matriculaRecebida

                        txtNome.text = "Olá, $nome"
                        txtMatricula.text = matricula
                    } else {
                        txtNome.text = "Usuário não encontrado"
                        txtMatricula.text = ""
                    }
                }
                .addOnFailureListener {
                    txtNome.text = "Erro ao carregar"
                }
        } else {
            txtNome.text = "Usuário"
        }

        //restante do codigo

        val btnHistorico = findViewById<LinearLayout>(R.id.btnHistorico)
        val btnFavoritos = findViewById<LinearLayout>(R.id.btnFavoritos)
        val btnAvisos = findViewById<LinearLayout>(R.id.btnAvisos)

        btnHistorico.setOnClickListener {
            startActivity(Intent(this, HistoricoEmprestimosUsuarioActivity::class.java))
        }

        btnFavoritos.setOnClickListener {
            startActivity(Intent(this, FavoritosUsuarioActivity::class.java))
        }

        btnAvisos.setOnClickListener {
            startActivity(Intent(this, AvisosUsuarioActivity::class.java))
        }

        val imagemPerfil = findViewById<ImageView>(R.id.leoFotoUser3)
        imagemPerfil.setOnClickListener {
            startActivity(Intent(this, PerfilUsuarioActivity::class.java))
        }

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


        // WABALABA DUB DUB
        val imagemLupa = findViewById<ImageView>(R.id.leoLupaPesquisa3)
        imagemLupa.setOnClickListener {

            val textoPesquisado = inputPesquisa.text?.toString()?.trim().orEmpty()
            if (textoPesquisado.isEmpty()) return@setOnClickListener

            val db = FirebaseFirestore.getInstance()

            db.collection("livros")
                .whereEqualTo("Titulo", textoPesquisado)
                .get()
                .addOnSuccessListener { result ->
                    if (!result.isEmpty) {

                        val doc = result.documents[0]

                        val intent = Intent(this, ResultadosPesquisaUsuarioActivity::class.java)

                        intent.putExtra("titulo", doc.getString("Titulo"))
                        intent.putExtra("autor", doc.getString("Autor"))
                        intent.putExtra("codigoAcervo", doc.getString("CodigoAcervo"))
                        intent.putExtra("disponibilidade", doc.getString("Disponibilidade"))
                        intent.putExtra("imagem", doc.getString("Imagem"))
                        intent.putExtra("quantidade", doc.getString("QuantidadeExemplares"))
                        intent.putExtra("situacaoEmprestimo", doc.getString("SituacaoEmprestimo"))
                        intent.putExtra("topicos", doc.getString("Topicos"))

                        startActivity(intent)

                    } else {
                        startActivity(Intent(this, MensagemSemResultadoUsuarioActivity::class.java))
                    }
                }
                .addOnFailureListener {
                    startActivity(Intent(this, MensagemSemResultadoUsuarioActivity::class.java))
                }
        }
    }
}
