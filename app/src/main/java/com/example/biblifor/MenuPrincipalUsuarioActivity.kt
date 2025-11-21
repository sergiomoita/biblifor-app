package com.example.biblifor

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
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
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MenuPrincipalUsuarioActivity : BaseActivity() {

    private val db = Firebase.firestore

    // ----- AVISOS -----
    private lateinit var rvUltimosAvisos: RecyclerView
    private lateinit var adapterAvisos: AvisoAdapter
    private val listaAvisos = mutableListOf<Aviso>()

    // ----- HISTÓRICO -----
    private lateinit var rvHistorico: RecyclerView
    private lateinit var adapterHistorico: HistoricoEmprestimoAdapter
    private val listaHistorico = mutableListOf<HistoricoEmprestimo>()
    private lateinit var emaHistorico: ImageView
    private lateinit var txtSemHistorico: TextView

    // ----- FAVORITOS -----
    private lateinit var rvFavoritos: RecyclerView
    private lateinit var adapterFavoritos: FavoritosAdapter

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
        // CONFIGURA AVISOS
        // ============================
        rvUltimosAvisos = findViewById(R.id.rvUltimosAvisos)
        rvUltimosAvisos.layoutManager = LinearLayoutManager(this)
        adapterAvisos = AvisoAdapter(listaAvisos)
        rvUltimosAvisos.adapter = adapterAvisos

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
        // CONFIGURA FAVORITOS
        // ============================
        rvFavoritos = findViewById(R.id.rvFavoritos)
        rvFavoritos.layoutManager = LinearLayoutManager(this)

        adapterFavoritos = FavoritosAdapter(mutableListOf()) { item ->
            // clique no favorito → vai para popup do livro
            val intent = Intent(this, PopupResultadosUsuarioActivity::class.java)
            intent.putExtra("livroId", item.livroId)
            startActivity(intent)
        }

        rvFavoritos.adapter = adapterFavoritos

        // ============================
        // CARREGAR DADOS DO ALUNO
        // ============================
        val prefs = getSharedPreferences("APP_PREFS", MODE_PRIVATE)
        val matricula = prefs.getString("MATRICULA_USER", null)
        val nome = prefs.getString("NOME_USER", null)

        val txtNome = findViewById<TextView>(R.id.leoOlaUsuario3)
        val txtMatricula = findViewById<TextView>(R.id.leoMatricula3)
        val imgFotoUser = findViewById<ImageView>(R.id.leoFotoUser3)

        txtNome.text = "Olá, ${nome ?: "Usuário"}"
        txtMatricula.text = matricula ?: "—"

        if (matricula != null) {
            carregarFotoPerfilAluno(matricula, imgFotoUser)
            carregarAvisos(matricula)
            carregarHistorico(matricula)
            carregarFavoritos(matricula)
        }

        // ============================
        // NAVEGAÇÃO
        // ============================

        // HOME — já estamos nela
        findViewById<ImageView>(R.id.leoLogoHome3).setOnClickListener { }

        findViewById<ImageView>(R.id.leoImagemChatbot3).setOnClickListener {
            startActivity(Intent(this, ChatbotUsuarioActivity::class.java))
        }

        findViewById<ImageView>(R.id.leoImagemNotificacoes3).setOnClickListener {
            startActivity(Intent(this, AvisosUsuarioActivity::class.java))
        }

        findViewById<ImageView>(R.id.leoImagemMenu3).setOnClickListener {
            startActivity(Intent(this, MenuHamburguerUsuarioActivity::class.java))
        }

        // --- BOTÕES "VER MAIS" ---
        findViewById<TextView>(R.id.btnVerMaisHistorico).setOnClickListener {
            startActivity(Intent(this, HistoricoEmprestimosUsuarioActivity::class.java))
        }

        findViewById<TextView>(R.id.btnVerMaisFavoritos).setOnClickListener {
            startActivity(Intent(this, FavoritosUsuarioActivity::class.java))
        }

        findViewById<TextView>(R.id.btnVerMaisAvisos).setOnClickListener {
            startActivity(Intent(this, AvisosUsuarioActivity::class.java))
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
                    val data =
                        doc.getString("dataEmprestimo")
                            ?: doc.getString("dataDevolucao")
                            ?: ""

                    listaHistorico.add(HistoricoEmprestimo("$titulo - $autor  $data"))
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
                    return@addOnSuccessListener
                }

                val listaFinal = mutableListOf<FavoritoItem>()
                var total = favoritosDocs.size()
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

                            // Quando todos tiverem sido processados → atualizar lista
                            if (processados == total) {
                                adapterFavoritos.updateList(listaFinal)
                            }
                        }
                }
            }
            .addOnFailureListener {
                Log.e("FAVORITOS", "Erro ao buscar favoritos: ${it.localizedMessage}")
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
                    if (bitmap != null) imageView.setImageBitmap(bitmap)
                }
            }
    }
}
