package com.example.biblifor

import android.content.Intent
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
        val nomeAdm = prefs.getString("NOME_ADM", "Administrador")

        // ====== Atualiza cabeçalho com nome e matrícula ======
        findViewById<TextView>(R.id.tvMatriculaHeader)?.text = matriculaAdm ?: ""
        findViewById<TextView>(R.id.tvNomeAdministradorEscreverMensagem)?.text = "Olá, $nomeAdm"

        if (matriculaAdm == null) {
            Toast.makeText(this, "Erro ao identificar administrador!", Toast.LENGTH_SHORT).show()
            return
        }

        // ====== Envio de mensagem ======
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

        // ====== BOTÃO VOLTAR ======
        findViewById<ImageView>(R.id.btnVoltarEscreverMensagemAdministradorSergio)?.setOnClickListener {
            finish()
        }

        // ====== FOTO DO ADM → PERFIL ======
        findViewById<ImageView>(R.id.fotoAdministradorEscreverMensagem)?.setOnClickListener {
            startActivity(Intent(this, PerfilAdministradorActivity::class.java))
        }

        // ====== BARRA INFERIOR ======
        findViewById<ImageView>(R.id.iconHomeEscreverMensagemAdministradorSergio)?.setOnClickListener {
            startActivity(Intent(this, MenuPrincipalAdministradorActivity::class.java))
            finish()
        }

        findViewById<ImageView>(R.id.iconEscreverEscreverMensagemAdministradorSergio)?.setOnClickListener {
            startActivity(Intent(this, EscreverMensagemAdministradorActivity::class.java))
        }

        findViewById<ImageView>(R.id.iconMensagemEscreverMensagemAdministradorSergio)?.setOnClickListener {
            startActivity(Intent(this, MensagensAdministradorActivity::class.java))
        }

        findViewById<ImageView>(R.id.iconMenuInferiorEscreverMensagemAdministradorSergio)?.setOnClickListener {
            startActivity(Intent(this, MenuHamburguerAdministradorActivity::class.java))
        }
    }
}
