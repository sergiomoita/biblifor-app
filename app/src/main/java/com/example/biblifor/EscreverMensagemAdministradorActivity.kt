package com.example.biblifor

import android.os.Bundle
import android.util.Log
import android.widget.*
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class EscreverMensagemAdministradorActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_escrever_mensagem_administrador)

        val etMatriculaDest = findViewById<EditText>(R.id.etDestinatariosEscreverMensagemAdministradorSergio)
        val etTitulo = findViewById<EditText>(R.id.etTituloEscreverMensagemAdministradorSergio)
        val etMensagem = findViewById<EditText>(R.id.etAssuntoEscreverMensagemAdministradorSergio)
        val btnEnviar = findViewById<Button>(R.id.btnEnviarEscreverMensagemAdministradorSergio)

        val prefs = getSharedPreferences("APP_PREFS", MODE_PRIVATE)
        val matriculaAdm = prefs.getString("MATRICULA_ADM", null)

        if (matriculaAdm == null) {
            Toast.makeText(this, "Erro ao identificar administrador!", Toast.LENGTH_SHORT).show()
            return
        }

        btnEnviar.setOnClickListener {

            val dest = etMatriculaDest.text.toString().trim()
            val titulo = etTitulo.text.toString().trim()
            val msg = etMensagem.text.toString().trim()

            if (dest.isEmpty() || titulo.isEmpty() || msg.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val dados = mapOf(
                "matricula" to dest,
                "matriculaAdm" to matriculaAdm,
                "titulo" to titulo,
                "mensagem" to msg,
                "data" to Timestamp.now()
            )

            Firebase.firestore.collection("mensagens")
                .add(dados)
                .addOnSuccessListener {
                    Toast.makeText(this, "Mensagem enviada!", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener { e ->
                    Log.e("FIRE", "Erro: ${e.localizedMessage}")
                }
        }
    }
}
