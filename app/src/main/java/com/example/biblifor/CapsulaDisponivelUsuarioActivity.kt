package com.example.biblifor

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CapsulaDisponivelUsuarioActivity : BaseActivity() {

    private val db by lazy { Firebase.firestore }
    private val CAPS = "capsulas"

    private object CapsStatus {
        const val DISP  = "Disponível"
        const val INDISP = "Indisponível"
    }

    private var numeroCapsula: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_capsula_disponivel_usuario)

        numeroCapsula = intent.getIntExtra("numeroCapsula", -1)
        if (numeroCapsula == -1) {
            Toast.makeText(this, "Cápsula inválida.", Toast.LENGTH_SHORT).show()
            finish(); return
        }

        // 🔙 Voltar
        val btnVoltar = findViewById<ImageView>(R.id.btnVoltarCapsulaDisponivelUsuarioSergio)
        btnVoltar.setOnClickListener { finish() }

        // 🎓 Botão "Reservar" → só aqui aparece o popup
        val btnReservar = findViewById<Button>(R.id.btnAcessarLoginUsuarioSergio)
        btnReservar.setOnClickListener { confirmarReserva() }
    }

    private fun confirmarReserva() {
        AlertDialog.Builder(this)
            .setTitle("Reservar cápsula $numeroCapsula?")
            .setMessage("A cápsula está disponível. Deseja reservar agora? Ela ficará indisponível para outros usuários.")
            .setPositiveButton("Reservar") { _, _ -> reservarCapsula() }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    /** Reserva com transação (Disponível -> Indisponível) e retorna RESULT_OK para pintar de vermelho */
    private fun reservarCapsula() {
        val ref = db.collection(CAPS).document("capsula$numeroCapsula")
        db.runTransaction { tx ->
            val snap = tx.get(ref)
            val atual = snap.getString("disponibilidade") ?: CapsStatus.INDISP
            if (atual != CapsStatus.DISP) throw IllegalStateException("Cápsula não está mais disponível.")
            tx.update(ref, "disponibilidade", CapsStatus.INDISP)
        }.addOnSuccessListener {
            mostrarToastReserva()
            setResult(RESULT_OK, Intent().apply {
                putExtra("numeroCapsula", numeroCapsula)
                putExtra("reservada", true)
            })
            finish()
        }.addOnFailureListener { e ->
            Toast.makeText(this, "Não foi possível reservar: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
        }
    }

    // 💬 Toast personalizado
    private fun mostrarToastReserva() {
        val inflater = LayoutInflater.from(this)
        val layout = inflater.inflate(R.layout.toast_sucesso_reserva, null)
        layout.findViewById<TextView>(R.id.tvMensagemToastSucesso)
            .text = "Cápsula de estudo reservada com sucesso!"
        Toast(this).apply {
            duration = Toast.LENGTH_SHORT
            view = layout
        }.show()
    }
}
