package com.example.biblifor

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CapsulasUsuarioActivity : BaseActivity() {

    private val db by lazy { Firebase.firestore }
    private val CAPS = "capsulas"

    private object CapsStatus {
        const val DISP  = "Disponível"
        const val INDISP = "Indisponível"
        const val MANUT = "Em manutenção"
    }

    // IDs das 8 cápsulas no layout do usuário
    private val idsCapsulas = listOf(
        R.id.btnCapsula1CapsulasUsuarioSergio,
        R.id.btnCapsula2CapsulasUsuarioSergio,
        R.id.btnCapsula3CapsulasUsuarioSergio,
        R.id.btnCapsula4CapsulasUsuarioSergio,
        R.id.btnCapsula5CapsulasUsuarioSergio,
        R.id.btnCapsula6CapsulasUsuarioSergio,
        R.id.btnCapsula7CapsulasUsuarioSergio,
        R.id.btnCapsula8CapsulasUsuarioSergio
    )

    // Recebe o resultado da tela "Disponível" (se reservou, pinta de vermelho)
    private val reservarLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                val numero = data?.getIntExtra("numeroCapsula", -1) ?: -1
                val reservada = data?.getBooleanExtra("reservada", false) ?: false
                if (reservada && numero in 1..idsCapsulas.size) {
                    aplicarEstadoVisual(numero, CapsStatus.INDISP) // muda para vermelho
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_capsulas_usuario)

        // 🔙 Voltar
        findViewById<ImageView>(R.id.btnVoltarCapsulasCapsulasUsuarioSergio).setOnClickListener { finish() }

        // 🤖 Mascote superior → Chatbot
        findViewById<ImageView>(R.id.iconMascoteCapsulasCapsulasUsuarioSergio).setOnClickListener {
            startActivity(Intent(this, ChatbotUsuarioActivity::class.java))
        }

        // 🔔 Notificações
        findViewById<ImageView>(R.id.iconNotificacaoCapsulasUsuarioSergio).setOnClickListener {
            startActivity(Intent(this, AvisosUsuarioActivity::class.java))
        }

        // ⚙️ Barra inferior
        findViewById<ImageView>(R.id.iconHomeCapsulasUsuarioSergio).setOnClickListener {
            startActivity(Intent(this, MenuPrincipalUsuarioActivity::class.java)); finish()
        }
        findViewById<ImageView>(R.id.iconMascoteInferiorCapsulasUsuarioSergio).setOnClickListener {
            startActivity(Intent(this, ChatbotUsuarioActivity::class.java))
        }
        findViewById<ImageView>(R.id.iconMensagemCapsulasUsuarioSergio).setOnClickListener {
            startActivity(Intent(this, AvisosUsuarioActivity::class.java))
        }
        findViewById<ImageView>(R.id.iconMenuInferiorCapsulasUsuarioSergio).setOnClickListener {
            startActivity(Intent(this, MenuPrincipalUsuarioActivity::class.java)); finish()
        }

        // 🎯 Clique nas cápsulas: consulta estado e navega para a tela correspondente
        idsCapsulas.forEachIndexed { idx, viewId ->
            val numero = idx + 1
            findViewById<ImageButton>(viewId).setOnClickListener {
                consultarEstadoRemotoENavegar(numero)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        sincronizarEstados()
    }

    override fun onResume() {
        super.onResume()
        // garante que a tela sempre reflita o estado do banco ao voltar
        sincronizarEstados()
    }

    // --------- Firestore (Aluno) ---------

    private fun docId(n: Int) = "capsula$n"

    /** Atualiza as cores de todas as cápsulas conforme Firestore */
    private fun sincronizarEstados() {
        for (n in 1..idsCapsulas.size) {
            db.collection(CAPS).document(docId(n)).get()
                .addOnSuccessListener { snap ->
                    val disp = snap.getString("disponibilidade") ?: CapsStatus.INDISP
                    aplicarEstadoVisual(n, disp)
                }
                .addOnFailureListener {
                    aplicarEstadoVisual(n, CapsStatus.INDISP)
                }
        }
    }

    /** Aplica background correto e grava uma tag simples para depuração */
    private fun aplicarEstadoVisual(numero: Int, estado: String) {
        val viewId = idsCapsulas.getOrNull(numero - 1) ?: return
        val btn = findViewById<ImageButton>(viewId)
        when (estado) {
            CapsStatus.DISP -> {
                btn.setBackgroundResource(R.drawable.bg_capsula_verde)
                btn.tag = "verde"
            }
            CapsStatus.MANUT -> {
                btn.setBackgroundResource(R.drawable.bg_capsula_amarelo)
                btn.tag = "amarela"
            }
            else -> {
                btn.setBackgroundResource(R.drawable.bg_capsula_vermelha)
                btn.tag = "vermelha"
            }
        }
    }

    private fun consultarEstadoRemotoENavegar(numero: Int) {
        db.collection(CAPS).document(docId(numero)).get()
            .addOnSuccessListener { snap ->
                val estado = snap.getString("disponibilidade") ?: CapsStatus.INDISP
                when (estado) {
                    CapsStatus.DISP -> {
                        val i = Intent(this, CapsulaDisponivelUsuarioActivity::class.java)
                        i.putExtra("numeroCapsula", numero)
                        reservarLauncher.launch(i) // popup e reserva acontecem na próxima tela
                    }
                    CapsStatus.MANUT -> {
                        startActivity(Intent(this, CapsulaManutencaoUsuarioActivity::class.java))
                    }
                    else -> {
                        startActivity(Intent(this, CapsulaIndisponivelUsuarioActivity::class.java))
                    }
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Falha ao consultar estado. Tente novamente.", Toast.LENGTH_SHORT).show()
            }
    }
}
