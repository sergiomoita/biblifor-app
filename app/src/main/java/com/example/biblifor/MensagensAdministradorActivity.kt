package com.example.biblifor

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.biblifor.adapter.AvisoAdapter
import com.example.biblifor.model.Aviso
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MensagensAdministradorActivity : BaseActivity() {

    private lateinit var fb: FirebaseFirestore
    private lateinit var rv: RecyclerView
    private lateinit var adapter: AvisoAdapter
    private val listaAvisos = mutableListOf<Aviso>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mensagens_administrador)

        fb = Firebase.firestore

        val prefs = getSharedPreferences("APP_PREFS", MODE_PRIVATE)
        val matriculaAdm = prefs.getString("MATRICULA_ADM", null)

        val txMatricula = findViewById<TextView>(R.id.textMatricula)

        if (matriculaAdm == null) {
            Log.e("ADM", "⚠️ Nenhuma matrícula ADM encontrada")
            txMatricula.text = ""
            return
        } else {
            txMatricula.text = matriculaAdm
        }

        rv = findViewById(R.id.rvEventosAdmin)
        rv.layoutManager = LinearLayoutManager(this)
        adapter = AvisoAdapter(listaAvisos)
        rv.adapter = adapter

        lerAvisos(matriculaAdm)

        // Navegação
        findViewById<ImageView>(R.id.iconNovaMensagem).setOnClickListener {
            startActivity(Intent(this, EscreverMensagemAdministradorActivity::class.java))
        }
    }

    private fun lerAvisos(matriculaAdm: String) {

        fb.collection("mensagens")
            .whereEqualTo("matriculaAdm", matriculaAdm)
            .orderBy("data", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->

                listaAvisos.clear()

                for (doc in result) {
                    val aviso = Aviso(
                        titulo = doc.getString("titulo") ?: "",
                        mensagem = doc.getString("mensagem") ?: "",
                        matricula = doc.getString("matricula") ?: "",
                        matriculaAdm = doc.getString("matriculaAdm") ?: "",
                        data = doc.getTimestamp("data")
                    )
                    listaAvisos.add(aviso)
                }

                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                Log.e("ADM", "❌ Erro ao buscar enviados: ${e.localizedMessage}")
            }
    }
}
