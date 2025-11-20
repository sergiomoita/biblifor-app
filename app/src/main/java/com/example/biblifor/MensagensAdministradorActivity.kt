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

        // ==== Recupera matrícula e nome do administrador logado ====
        val prefs = getSharedPreferences("APP_PREFS", MODE_PRIVATE)
        val matriculaAdm = prefs.getString("MATRICULA_ADM", null)
        val nomeAdm = prefs.getString("NOME_ADM", null)

        val txMatricula = findViewById<TextView>(R.id.textMatricula)
        val txNomeAdm = findViewById<TextView>(R.id.textNomeAdministradorAvisos)

        txNomeAdm.text = if (!nomeAdm.isNullOrEmpty()) "Olá, $nomeAdm" else "Olá, Administrador"
        txMatricula.text = matriculaAdm ?: ""

        // ======================================================
        // FOTO DO ADMINISTRADOR (CORRIGIDO para coleção "administrador")
        // ======================================================
        val imgAdm = findViewById<ImageView>(R.id.imageView3)

        if (!matriculaAdm.isNullOrEmpty()) {
            fb.collection("administrador") // <<< mesmo nome usado nas outras telas
                .document(matriculaAdm)
                .get()
                .addOnSuccessListener { doc ->
                    if (doc.exists()) {
                        val base64Raw = doc.getString("fotoPerfil")
                        if (!base64Raw.isNullOrEmpty()) {
                            try {
                                val base64 = base64Raw
                                    .replace("data:image/jpeg;base64,", "")
                                    .replace("data:image/png;base64,", "")
                                    .replace("\"", "")
                                    .trim()

                                val bytes = android.util.Base64.decode(
                                    base64,
                                    android.util.Base64.DEFAULT
                                )
                                val bmp = android.graphics.BitmapFactory.decodeByteArray(
                                    bytes, 0, bytes.size
                                )
                                if (bmp != null) imgAdm.setImageBitmap(bmp)
                            } catch (e: Exception) {
                                Log.e("FOTO_ADM", "Erro ao decodificar Base64: ${e.message}")
                            }
                        }
                    }
                }
                .addOnFailureListener {
                    Log.e("ADM", "Erro ao carregar foto: ${it.localizedMessage}")
                }
        }

        // ==== Configura RecyclerView ====
        rv = findViewById(R.id.rvEventosAdmin)
        rv.layoutManager = LinearLayoutManager(this)
        adapter = AvisoAdapter(listaAvisos)
        rv.adapter = adapter

        // ==== Busca avisos enviados pelo administrador logado ====
        if (matriculaAdm != null) {
            lerAvisos(matriculaAdm)
        }

        // ==== Botões ====
        findViewById<ImageView>(R.id.iconNovaMensagem).setOnClickListener {
            startActivity(Intent(this, EscreverMensagemAdministradorActivity::class.java))
        }

        imgAdm.setOnClickListener {
            startActivity(Intent(this, PerfilAdministradorActivity::class.java))
        }

        findViewById<ImageView>(R.id.iconHomeCapsulasAdmSergio).setOnClickListener {
            startActivity(Intent(this, MenuPrincipalAdministradorActivity::class.java))
            finish()
        }

        findViewById<ImageView>(R.id.iconEscreverMsgCapsulasAdmSergio).setOnClickListener {
            startActivity(Intent(this, EscreverMensagemAdministradorActivity::class.java))
        }

        findViewById<ImageView>(R.id.iconMensagemCapsulasAdmSergio).setOnClickListener {
            startActivity(Intent(this, MensagensAdministradorActivity::class.java))
        }

        findViewById<ImageView>(R.id.iconMenuInferiorCapsulasAdmSergio).setOnClickListener {
            startActivity(Intent(this, MenuHamburguerAdministradorActivity::class.java))
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
