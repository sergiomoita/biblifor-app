package com.example.biblifor

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.biblifor.adapter.AvisoAdapter
import com.example.biblifor.model.Aviso
import com.example.biblifor.util.base64ToBitmap
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.ktx.firestore
import java.text.Normalizer

class MenuPrincipalUsuarioActivity : BaseActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var rvUltimosAvisos: RecyclerView
    private lateinit var adapterAvisos: AvisoAdapter
    private val listaUltimosAvisos = mutableListOf<Aviso>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu_principal_usuario)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // ==== Inicializa Firestore ====
        db = Firebase.firestore

        // ==== Configuração da RecyclerView ====
        rvUltimosAvisos = findViewById(R.id.rvUltimosAvisos)
        rvUltimosAvisos.layoutManager = LinearLayoutManager(this)
        adapterAvisos = AvisoAdapter(listaUltimosAvisos)
        rvUltimosAvisos.adapter = adapterAvisos

        // ==== Recupera nome e matrícula do usuário logado ====
        val prefs = getSharedPreferences("APP_PREFS", MODE_PRIVATE)
        val matriculaUser = prefs.getString("MATRICULA_USER", null)
        val nomeUser = prefs.getString("NOME_USER", null)

        // ==== Atualiza cabeçalho ====
        val txtNome = findViewById<TextView>(R.id.leoOlaUsuario3)
        val txtMatricula = findViewById<TextView>(R.id.leoMatricula3)
        val imgFotoUser = findViewById<ImageView>(R.id.leoFotoUser3)

        if (!nomeUser.isNullOrEmpty()) {
            txtNome.text = "Olá, $nomeUser"
        } else {
            txtNome.text = "Olá, usuário"
        }

        txtMatricula.text = matriculaUser ?: "—"

        // ==== Carrega foto de perfil do aluno logado ====
        if (matriculaUser != null) {
            carregarFotoPerfilAluno(matriculaUser, imgFotoUser)
            carregarUltimosAvisos(matriculaUser)
        } else {
            Log.e("AVISOS", "⚠️ Nenhuma matrícula de usuário encontrada")
        }

        // ==== BOTÕES DE SEÇÕES ====
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

        // ==== PERFIL ====
        imgFotoUser.setOnClickListener {
            startActivity(Intent(this, PerfilUsuarioActivity::class.java))
        }

        // ==== ACESSIBILIDADE ====
        val imagemAcessibilidade = findViewById<ImageView>(R.id.leoAcessibilidade3)
        val inputPesquisa = findViewById<EditText>(R.id.leoPesquisa3)

        val textosParaAcessibilidade = listOf<TextView>(
            findViewById(R.id.leotextViewUnifor3),
            findViewById(R.id.leoOlaUsuario3),
            findViewById(R.id.leoMatricula3),
            findViewById(R.id.leoTituloHistorico3),
            findViewById(R.id.leoLegendaEmaDeitada3),
            findViewById(R.id.leoFavoritos3),
            findViewById(R.id.leoUltimosAvisos3)
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

        // ==== NAVEGAÇÃO INFERIOR ====
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

        // ==== PESQUISA ====
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

    // ==== LER 3 ÚLTIMOS AVISOS ====
    private fun carregarUltimosAvisos(matricula: String) {
        db.collection("mensagens")
            .whereEqualTo("matricula", matricula)
            .orderBy("data", Query.Direction.DESCENDING)
            .limit(3)
            .get()
            .addOnSuccessListener { result ->
                listaUltimosAvisos.clear()
                for (doc in result) {
                    val aviso = Aviso(
                        titulo = doc.getString("titulo") ?: "",
                        mensagem = doc.getString("mensagem") ?: "",
                        matricula = doc.getString("matricula") ?: "",
                        matriculaAdm = doc.getString("matriculaAdm") ?: "",
                        data = doc.getTimestamp("data")
                    )
                    listaUltimosAvisos.add(aviso)
                }
                adapterAvisos.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                Log.e("AVISOS", "❌ Erro ao carregar avisos: ${e.localizedMessage}")
            }
    }

    // ==== NOVO: carregar fotoPerfil do aluno logado ====
    private fun carregarFotoPerfilAluno(matricula: String, imageView: ImageView) {
        db.collection("alunos")
            .document(matricula)
            .get()
            .addOnSuccessListener { doc ->
                if (doc != null && doc.exists()) {
                    val fotoBase64 = doc.getString("fotoPerfil")
                    if (!fotoBase64.isNullOrEmpty()) {
                        val bitmap = base64ToBitmap(fotoBase64)
                        if (bitmap != null) {
                            imageView.setImageBitmap(bitmap)
                        } else {
                            Log.e("FOTO_PERFIL", "Falha ao decodificar bitmap para $matricula")
                        }
                    } else {
                        Log.d("FOTO_PERFIL", "fotoPerfil vazio para $matricula")
                    }
                } else {
                    Log.e("FOTO_PERFIL", "Documento de aluno não encontrado para $matricula")
                }
            }
            .addOnFailureListener { e ->
                Log.e("FOTO_PERFIL", "Erro ao buscar foto: ${e.localizedMessage}")
            }
    }
}
