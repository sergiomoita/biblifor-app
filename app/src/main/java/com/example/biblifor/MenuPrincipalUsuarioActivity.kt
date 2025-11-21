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
        // REFS DE ACESSIBILIDADE / PESQUISA
        // ============================
        imagemAcessibilidade = findViewById(R.id.leoAcessibilidade3)
        inputPesquisa = findViewById(R.id.leoPesquisa3)
        val imagemLupa = findViewById<ImageView>(R.id.leoLupaPesquisa3)

        // Textos que mudam de cor na acessibilidade
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
        // BOTÕES DE SEÇÕES / "VER MAIS"
        // ============================
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
            // Reinicia / volta pra home
            startActivity(Intent(this, MenuPrincipalUsuarioActivity::class.java))
        }

        findViewById<ImageView>(R.id.leoImagemChatbot3).setOnClickListener {
            startActivity(Intent(this, ChatbotUsuarioActivity::class.java))
        }

        // Ícone de email da barra inferior
        findViewById<ImageView>(R.id.leoImagemNotificacoes3).setOnClickListener {
            startActivity(Intent(this, AvisosUsuarioActivity::class.java))
        }

        // Ícone de sino lá em cima
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
        // CONFIGURA HISTÓRICO
        // ============================
        rvHistorico = findViewById(R.id.rvHistorico)
        emaHistorico = findViewById(R.id.leoEmaSemHistorico)
        txtSemHistorico = findViewById(R.id.leoTextoSemHistorico)

        adapterHistorico = HistoricoEmprestimoAdapter(listaHistorico)
        rvHistorico.layoutManager = LinearLayoutManager(this)
        rvHistorico.adapter = adapterHistorico

        // ============================
        // CONFIGURA FAVORITOS + TEXTO "SEM FAVORITOS"
        // ============================
        rvFavoritos = findViewById(R.id.rvFavoritos)
        rvFavoritos.layoutManager = LinearLayoutManager(this)

        adapterFavoritos = FavoritosAdapter(mutableListOf()) { item ->
            val intent = Intent(this, PopupResultadosUsuarioActivity::class.java)
            intent.putExtra("livroId", item.livroId)
            startActivity(intent)
        }
        rvFavoritos.adapter = adapterFavoritos

        // Criar TextView "sem favoritos" logo abaixo do RecyclerView (via código)
        txtSemFavoritos = TextView(this).apply {
            text = "Você não possui livros favoritados"
            setTextColor(Color.WHITE)
            textSize = 14f
            gravity = Gravity.CENTER_HORIZONTAL
            visibility = View.GONE

            // distância do título
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
        // CONFIGURA AVISOS + TEXTO "SEM AVISOS"
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

            // distância do título
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
        // CARREGAR DADOS DO ALUNO
        // ============================
        if (matricula != null) {
            carregarFotoPerfilAluno(matricula, imgFotoUser)
            carregarAvisos(matricula)
            carregarHistorico(matricula)
            carregarFavoritos(matricula)
        } else {
            Log.e("MENU_USUARIO", "Nenhuma matrícula encontrada nas prefs")
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
            .addOnFailureListener {
                Log.e("HISTORICO", "Erro ao carregar histórico: ${it.localizedMessage}")
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
            .addOnFailureListener {
                Log.e("FAVORITOS", "Erro ao buscar favoritos: ${it.localizedMessage}")
                rvFavoritos.visibility = View.GONE
                txtSemFavoritos.visibility = View.VISIBLE
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
            .addOnFailureListener {
                Log.e("AVISOS", "Erro ao buscar avisos: ${it.localizedMessage}")
                rvUltimosAvisos.visibility = View.GONE
                txtSemAvisos.visibility = View.VISIBLE
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
                    } else {
                        Log.e("FOTO_PERFIL", "Falha ao decodificar bitmap para $matricula")
                    }
                } else {
                    Log.d("FOTO_PERFIL", "fotoPerfil vazio para $matricula")
                }
            }
            .addOnFailureListener {
                Log.e("FOTO_PERFIL", "Erro ao buscar foto: ${it.localizedMessage}")
            }
    }
}
