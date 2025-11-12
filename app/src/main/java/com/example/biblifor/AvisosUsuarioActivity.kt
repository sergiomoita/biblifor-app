package com.example.biblifor

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.biblifor.adapter.AvisoAdapter
import com.example.biblifor.model.Aviso
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AvisosUsuarioActivity : BaseActivity() {

    private lateinit var fb: FirebaseFirestore
    private lateinit var rv: RecyclerView
    private lateinit var adapter: AvisoAdapter
    private val listaAvisos = mutableListOf<Aviso>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_avisos_usuario)

        fb = Firebase.firestore

        // ===== RecyclerView =====
        rv = findViewById(R.id.rvAvisosUsuario)
        rv.layoutManager = LinearLayoutManager(this)
        adapter = AvisoAdapter(listaAvisos)
        rv.adapter = adapter

        // ✅ Recupera matrícula e nome do usuário logado
        val prefs = getSharedPreferences("APP_PREFS", MODE_PRIVATE)
        val matriculaUser = prefs.getString("MATRICULA_USER", "") ?: ""
        val nomeUser = prefs.getString("NOME_USER", null)

        // ✅ Exibe nome e matrícula no topo da tela
        val txtMatricula = findViewById<TextView>(R.id.textMatricula)
        val txtNomeUsuario = findViewById<TextView>(R.id.textNomeUsuarioAvisos)

        txtMatricula.text = matriculaUser
        txtNomeUsuario.text = if (!nomeUser.isNullOrEmpty()) "Olá, $nomeUser" else "Olá, Usuário"

        // ✅ Buscar avisos
        lerAvisos(matriculaUser)

        configurarBotoes()
    }

    // ✅ Função principal que busca os avisos no Firestore
    private fun lerAvisos(matricula: String) {
        fb.collection("mensagens")
            .whereEqualTo("matricula", matricula)
            .orderBy("data", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                listaAvisos.clear()

                for (doc in result) {
                    val aviso = Aviso(
                        titulo = doc.getString("titulo") ?: "(sem título)",
                        data = doc.getTimestamp("data"),
                        mensagem = doc.getString("mensagem") ?: "(sem mensagem)",
                        matricula = doc.getString("matricula") ?: "",
                        matriculaAdm = doc.getString("matriculaAdm") ?: ""
                    )
                    listaAvisos.add(aviso)
                }

                adapter.notifyDataSetChanged()

                if (listaAvisos.isEmpty()) {
                    Toast.makeText(this, "Nenhum aviso encontrado.", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Erro: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
            }
    }

    // ===== BOTÕES DA TELA =====
    private fun configurarBotoes() {
        findViewById<ImageView>(R.id.leoLogoHomeChatbotBF7).setOnClickListener {
            startActivity(Intent(this, MenuPrincipalUsuarioActivity::class.java))
        }

        findViewById<ImageView>(R.id.leoImagemChatbotBF7).setOnClickListener {
            startActivity(Intent(this, ChatbotUsuarioActivity::class.java))
        }

        findViewById<ImageView>(R.id.leoImagemNotificacoesChatbotBF7).setOnClickListener {
            startActivity(Intent(this, AvisosUsuarioActivity::class.java))
        }

        findViewById<ImageView>(R.id.leoImagemMenuChatbotBF7).setOnClickListener {
            startActivity(Intent(this, MenuHamburguerUsuarioActivity::class.java))
        }

        findViewById<ImageView>(R.id.imageView3).setOnClickListener {
            startActivity(Intent(this, PerfilUsuarioActivity::class.java))
        }

        findViewById<ImageView>(R.id.imageView4).setOnClickListener {
            startActivity(Intent(this, ChatbotUsuarioActivity::class.java))
        }

        findViewById<ImageView>(R.id.imageView5).setOnClickListener {
            startActivity(Intent(this, AvisosUsuarioActivity::class.java))
        }
    }
}
