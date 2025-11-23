package com.example.biblifor

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
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
import com.example.biblifor.adapter.FavoritosAdapter
import com.example.biblifor.model.Aviso
import com.example.biblifor.util.base64ToBitmap
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.ktx.firestore

class MenuPrincipalUsuarioActivity : BaseActivity() {

    private val db = Firebase.firestore

    // ----- AVISOS -----
    private lateinit var rvUltimosAvisos: RecyclerView
    private lateinit var adapterAvisos: AvisoAdapter
    private val listaAvisos = mutableListOf<Aviso>()
    private lateinit var txtSemAvisos: TextView

    // ----- HISTÓRICO -----
    private lateinit var rvHistorico: RecyclerView
    private lateinit var adapterHistorico: HistoricoEmprestimoAdapter
    private val listaHistorico = mutableListOf<HistoricoEmprestimo>()
    private lateinit var emaHistorico: ImageView
    private lateinit var txtSemHistorico: TextView

    // ----- FAVORITOS -----
    private lateinit var rvFavoritos: RecyclerView
    private lateinit var adapterFavoritos: FavoritosAdapter
    private lateinit var txtSemFavoritos: TextView

    // ----- PESQUISA / ACESSIBILIDADE -----
    private lateinit var inputPesquisa: EditText
    private lateinit var imagemAcessibilidade: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu_principal_usuario)

        val bolinhaCima = findViewById<ImageView>(R.id.bolinhaCima)
        val bolinhaBaixo = findViewById<ImageView>(R.id.bolinhaBaixo)


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // ============================
        // CABEÇALHO / PERFIL
        // ============================
        val prefs = getSharedPreferences("APP_PREFS", MODE_PRIVATE)
        val matricula = prefs.getString("MATRICULA_USER", null)
        val nome = prefs.getString("NOME_USER", null)

        val txtNome = findViewById<TextView>(R.id.leoOlaUsuario3)
        val txtMatricula = findViewById<TextView>(R.id.leoMatricula3)
        val imgFotoUser = findViewById<ImageView>(R.id.leoFotoUser3)

        txtNome.text = if (!nome.isNullOrEmpty()) "Olá, $nome" else "Olá, usuário"
        txtMatricula.text = matricula ?: "—"

        // Clique no perfil
        imgFotoUser.setOnClickListener {
            startActivity(Intent(this, PerfilUsuarioActivity::class.java))
        }

        // ============================
        // ACESSIBILIDADE E PESQUISA
        // ============================
        imagemAcessibilidade = findViewById(R.id.leoAcessibilidade3)
        inputPesquisa = findViewById(R.id.leoPesquisa3)
        val imagemLupa = findViewById<ImageView>(R.id.leoLupaPesquisa3)

        val textosParaAcessibilidade = listOf<TextView>(
            findViewById(R.id.leotextViewUnifor3),
            findViewById(R.id.leoOlaUsuario3),
            findViewById(R.id.leoMatricula3),
            findViewById(R.id.leoTituloHistorico3),
            findViewById(R.id.leoTextoSemHistorico),
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

        // ============================
        // BOTÕES DE SEÇÃO / VER MAIS
        // ============================
        findViewById<LinearLayout>(R.id.btnHistorico).setOnClickListener {
            startActivity(Intent(this, HistoricoEmprestimosUsuarioActivity::class.java))
        }

        findViewById<LinearLayout>(R.id.btnFavoritos).setOnClickListener {
            startActivity(Intent(this, FavoritosUsuarioActivity::class.java))
        }

        findViewById<LinearLayout>(R.id.btnAvisos).setOnClickListener {
            startActivity(Intent(this, AvisosUsuarioActivity::class.java))
        }

        findViewById<TextView>(R.id.btnVerMaisHistorico).setOnClickListener {
            startActivity(Intent(this, HistoricoEmprestimosUsuarioActivity::class.java))
        }

        findViewById<TextView>(R.id.btnVerMaisFavoritos).setOnClickListener {
            startActivity(Intent(this, FavoritosUsuarioActivity::class.java))
        }

        findViewById<TextView>(R.id.btnVerMaisAvisos).setOnClickListener {
            startActivity(Intent(this, AvisosUsuarioActivity::class.java))
        }

        // ============================
        // NAVEGAÇÃO INFERIOR
        // ============================
        findViewById<ImageView>(R.id.leoLogoHome3).setOnClickListener {
            startActivity(Intent(this, MenuPrincipalUsuarioActivity::class.java))
        }

        findViewById<ImageView>(R.id.leoImagemChatbot3).setOnClickListener {
            startActivity(Intent(this, ChatbotUsuarioActivity::class.java))
        }

        findViewById<ImageView>(R.id.leoImagemNotificacoes3).setOnClickListener {
            startActivity(Intent(this, AvisosUsuarioActivity::class.java))
        }

        findViewById<ImageView>(R.id.leoNotificacao3).setOnClickListener {
            startActivity(Intent(this, AvisosUsuarioActivity::class.java))
        }

        findViewById<ImageView>(R.id.leoImagemMenu3).setOnClickListener {
            startActivity(Intent(this, MenuHamburguerUsuarioActivity::class.java))
        }

        // ============================
        // PESQUISA INTELIGENTE
        // ============================
        imagemLupa.setOnClickListener {
            val termo = inputPesquisa.text?.toString()?.trim().orEmpty()
            if (termo.isBlank()) return@setOnClickListener

            val intent = Intent(this, ResultadosPesquisaUsuarioActivity::class.java)
            intent.putExtra("pesquisa", termo)
            startActivity(intent)
        }

        // ============================
        // CONFIGURAR HISTÓRICO
        // ============================
        rvHistorico = findViewById(R.id.rvHistorico)
        emaHistorico = findViewById(R.id.leoEmaSemHistorico)
        txtSemHistorico = findViewById(R.id.leoTextoSemHistorico)

        adapterHistorico = HistoricoEmprestimoAdapter(listaHistorico)
        rvHistorico.layoutManager = LinearLayoutManager(this)
        rvHistorico.adapter = adapterHistorico

        // ============================
        // CONFIGURAR FAVORITOS (SEM CLIQUE)
        // ============================
        rvFavoritos = findViewById(R.id.rvFavoritos)
        rvFavoritos.layoutManager = LinearLayoutManager(this)

        // 🔥 FAVORITOS SEM QUALQUER AÇÃO DE CLIQUE
        adapterFavoritos = FavoritosAdapter(mutableListOf()) { /* clique desativado */ }

        rvFavoritos.adapter = adapterFavoritos

        txtSemFavoritos = TextView(this).apply {
            text = "Você não possui livros favoritados"
            setTextColor(Color.WHITE)
            textSize = 14f
            gravity = Gravity.CENTER_HORIZONTAL
            visibility = View.GONE

            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.topMargin = (16 * resources.displayMetrics.density).toInt()
            layoutParams = params
        }

        (rvFavoritos.parent as? ViewGroup)?.let { parent ->
            val index = parent.indexOfChild(rvFavoritos)
            parent.addView(txtSemFavoritos, index + 1)
        }

        // ============================
        // CONFIGURAR AVISOS
        // ============================
        rvUltimosAvisos = findViewById(R.id.rvUltimosAvisos)
        rvUltimosAvisos.layoutManager = LinearLayoutManager(this)
        adapterAvisos = AvisoAdapter(listaAvisos)
        rvUltimosAvisos.adapter = adapterAvisos

        txtSemAvisos = TextView(this).apply {
            text = "Você não possui avisos recentes"
            setTextColor(Color.WHITE)
            textSize = 14f
            gravity = Gravity.CENTER_HORIZONTAL
            visibility = View.GONE

            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.topMargin = (16 * resources.displayMetrics.density).toInt()
            layoutParams = params
        }

        (rvUltimosAvisos.parent as? ViewGroup)?.let { parent ->
            val index = parent.indexOfChild(rvUltimosAvisos)
            parent.addView(txtSemAvisos, index + 1)
        }

        // ============================
        // CARREGAR DADOS DO USUÁRIO
        // ============================
        if (matricula != null) {
            carregarFotoPerfilAluno(matricula, imgFotoUser)
            carregarAvisos(matricula)
            carregarHistorico(matricula)
            carregarFavoritos(matricula)
        }

        if (matricula != null) {
            verificarNovasMensagens(matricula, bolinhaCima, bolinhaBaixo)
        }

    }

    // ======================================================
    // HISTÓRICO
    // ======================================================
    private fun carregarHistorico(matricula: String) {
        db.collection("alunos")
            .document(matricula)
            .collection("historicoEmprestimos")
            .limit(3)
            .get()
            .addOnSuccessListener { result ->
                listaHistorico.clear()

                for (doc in result) {
                    val titulo = doc.getString("nome") ?: "Sem título"
                    val autor = doc.getString("autor") ?: ""
                    val data = doc.getString("dataEmprestimo")
                        ?: doc.getString("dataDevolucao")
                        ?: ""

                    listaHistorico.add(
                        HistoricoEmprestimo("$titulo - $autor  $data")
                    )
                }

                adapterHistorico.notifyDataSetChanged()

                if (listaHistorico.isEmpty()) {
                    emaHistorico.visibility = View.VISIBLE
                    txtSemHistorico.visibility = View.VISIBLE
                    rvHistorico.visibility = View.GONE
                } else {
                    emaHistorico.visibility = View.GONE
                    txtSemHistorico.visibility = View.GONE
                    rvHistorico.visibility = View.VISIBLE
                }
            }
    }

    // ======================================================
    // FAVORITOS
    // ======================================================
    private fun carregarFavoritos(matricula: String) {
        db.collection("alunos")
            .document(matricula)
            .collection("favoritos")
            .whereEqualTo("favorito", true)
            .get()
            .addOnSuccessListener { favoritosDocs ->

                if (favoritosDocs.isEmpty) {
                    adapterFavoritos.updateList(emptyList())
                    rvFavoritos.visibility = View.GONE
                    txtSemFavoritos.visibility = View.VISIBLE
                    return@addOnSuccessListener
                }

                val listaFinal = mutableListOf<FavoritoItem>()
                val total = favoritosDocs.size()
                var processados = 0

                for (favDoc in favoritosDocs) {
                    val livroId = favDoc.id

                    db.collection("livros").document(livroId)
                        .get()
                        .addOnSuccessListener { livro ->
                            if (livro.exists()) {
                                val titulo = livro.getString("Titulo") ?: livroId
                                val disponibilidade = livro.getString("Disponibilidade") ?: "—"

                                listaFinal.add(
                                    FavoritoItem(
                                        titulo = titulo,
                                        disponibilidade = disponibilidade,
                                        livroId = livroId
                                    )
                                )
                            }
                        }
                        .addOnCompleteListener {
                            processados++
                            if (processados == total) {
                                if (listaFinal.isEmpty()) {
                                    rvFavoritos.visibility = View.GONE
                                    txtSemFavoritos.visibility = View.VISIBLE
                                    adapterFavoritos.updateList(emptyList())
                                } else {
                                    rvFavoritos.visibility = View.VISIBLE
                                    txtSemFavoritos.visibility = View.GONE
                                    adapterFavoritos.updateList(listaFinal)
                                }
                            }
                        }
                }
            }
    }

    // ======================================================
    // AVISOS
    // ======================================================
    private fun carregarAvisos(matricula: String) {
        db.collection("mensagens")
            .whereEqualTo("matricula", matricula)
            .orderBy("data", Query.Direction.DESCENDING)
            .limit(3)
            .get()
            .addOnSuccessListener { result ->
                listaAvisos.clear()

                for (doc in result) {
                    listaAvisos.add(
                        Aviso(
                            titulo = doc.getString("titulo") ?: "",
                            mensagem = doc.getString("mensagem") ?: "",
                            matricula = matricula,
                            matriculaAdm = doc.getString("matriculaAdm") ?: "",
                            data = doc.getTimestamp("data")
                        )
                    )
                }

                adapterAvisos.notifyDataSetChanged()

                if (listaAvisos.isEmpty()) {
                    rvUltimosAvisos.visibility = View.GONE
                    txtSemAvisos.visibility = View.VISIBLE
                } else {
                    rvUltimosAvisos.visibility = View.VISIBLE
                    txtSemAvisos.visibility = View.GONE
                }
            }
    }

    // ======================================================
    // FOTO PERFIL
    // ======================================================
    private fun carregarFotoPerfilAluno(matricula: String, imageView: ImageView) {
        db.collection("alunos").document(matricula)
            .get()
            .addOnSuccessListener { doc ->
                val foto = doc.getString("fotoPerfil")
                if (!foto.isNullOrEmpty()) {
                    val bitmap = base64ToBitmap(foto)
                    if (bitmap != null) {
                        imageView.setImageBitmap(bitmap)
                    }
                }
            }
    }

    private fun verificarNovasMensagens(
        matricula: String,
        bolinhaCima: ImageView,
        bolinhaBaixo: ImageView
    ) {
        val prefs = getSharedPreferences("APP_PREFS", MODE_PRIVATE)
        val ultimaLeitura = prefs.getLong("ULTIMA_LEITURA_AVISOS", 0)

        db.collection("mensagens")
            .whereEqualTo("matricula", matricula)
            .orderBy("data", Query.Direction.DESCENDING)
            .limit(1)
            .get()
            .addOnSuccessListener { docs ->

                if (!docs.isEmpty) {
                    val msgNova = docs.first().getTimestamp("data")?.toDate()?.time ?: 0

                    val temNova = msgNova > ultimaLeitura

                    bolinhaCima.visibility = if (temNova) View.VISIBLE else View.GONE
                    bolinhaBaixo.visibility = if (temNova) View.VISIBLE else View.GONE
                }
            }
    }

}
