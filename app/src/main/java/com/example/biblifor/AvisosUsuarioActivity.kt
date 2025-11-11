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

    lateinit var fb: FirebaseFirestore
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

        // ✅ Pegando matrícula salva no login
        val prefs = getSharedPreferences("APP_PREFS", MODE_PRIVATE)
        val matriculaUser = prefs.getString("MATRICULA_USER", "") ?: ""

        val txtMatricula = findViewById<TextView>(R.id.textMatricula)
        txtMatricula.text = matriculaUser

        // ✅ Buscar avisos
        lerAvisos(matriculaUser)

        configurarBotoes()
    }

    // ✅ Função principal que busca do Firestore
    fun lerAvisos(matricula: String) {

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

                Toast.makeText(this, "Mensagens: ${result.size()}", Toast.LENGTH_LONG).show()

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
