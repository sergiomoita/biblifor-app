package com.example.biblifor

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import java.text.Normalizer
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CapsulasAdministradorActivity : BaseActivity() {

    private val db by lazy { Firebase.firestore }
    private val CAPS = "capsulas"

    private object CapsStatus {
        const val DISP  = "Disponível"
        const val INDISP = "Indisponível"
        const val MANUT = "Em manutenção"   // novo texto solicitado
    }

    // Estado das 8 cápsulas (1..8; índice 0 descartado) — valores: DISPONIVEL/INDISPONIVEL/MANUTENCAO
    private var statusCapsulas: MutableList<String> = MutableList(9) { "DISPONIVEL" }

    // Recebe o novo status da tela de edição e persiste no Firestore
    private val statusLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data ?: return@registerForActivityResult
                val numero = data.getIntExtra("numeroCapsula", -1)
                val novoStatus = (data.getStringExtra("novoStatus") ?: return@registerForActivityResult).normalizado()

                val code = when (novoStatus) {
                    "disponivel"   -> "DISPONIVEL"
                    "indisponivel" -> "INDISPONIVEL"
                    "manutencao", "emmanutencao" -> "MANUTENCAO" // aceita ambos, grava como "Em manutenção"
                    else           -> "DISPONIVEL"
                }

                if (numero in 1..8) {
                    atualizarStatusNoFirestore(numero, code)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_capsulas_administrador)

        // Restaura estado em recriações
        if (savedInstanceState != null) {
            val arr = savedInstanceState.getStringArray("statusCapsulas")
            if (arr != null && arr.size == 9) statusCapsulas = arr.toMutableList()
        }

        // 🔙 Voltar
        findViewById<ImageView>(R.id.btnVoltarCapsulasAdmSergio).setOnClickListener {
            startActivity(Intent(this, MenuPrincipalAdministradorActivity::class.java))
            finish()
        }

        // ⚙️ Barra inferior
        findViewById<ImageView>(R.id.iconHomeCapsulasAdmSergio).setOnClickListener {
            startActivity(Intent(this, MenuPrincipalAdministradorActivity::class.java)); finish()
        }
        findViewById<ImageView>(R.id.iconEscreverMsgCapsulasAdmSergio).setOnClickListener {
            startActivity(Intent(this, EscreverMensagemAdministradorActivity::class.java))
        }
        findViewById<ImageView>(R.id.iconMensagemCapsulasAdmSergio).setOnClickListener {
            startActivity(Intent(this, MensagensAdministradorActivity::class.java))
        }
        findViewById<ImageView>(R.id.iconMenuInferiorCapsulasAdmSergio).setOnClickListener {
            startActivity(Intent(this, MenuHamburguerAdministradorActivity::class.java)); finish()
        }

        // 💊 Clique nas cápsulas → abre a tela de status atual
        val idsCapsulas = listOf(
            R.id.btnCapsula1AdmCapsulasAdmSergio,
            R.id.btnCapsula2AdmCapsulasAdmSergio,
            R.id.btnCapsula3AdmCapsulasAdmSergio,
            R.id.btnCapsula4AdmCapsulasAdmSergio,
            R.id.btnCapsula5AdmCapsulasAdmSergio,
            R.id.btnCapsula6AdmCapsulasAdmSergio,
            R.id.btnCapsula7AdmCapsulasAdmSergio,
            R.id.btnCapsula8AdmCapsulasAdmSergio
        )

        idsCapsulas.forEachIndexed { index, idBtn ->
            findViewById<ImageButton>(idBtn).setOnClickListener {
                val numero = index + 1
                val statusBonito = when (statusCapsulas[numero]) {
                    "DISPONIVEL"   -> "Disponível"
                    "INDISPONIVEL" -> "Indisponível"
                    "MANUTENCAO"   -> "Em manutenção"
                    else           -> "Disponível"
                }
                val i = Intent(this, StatusCapsulaAdministradorActivity::class.java)
                i.putExtra("numeroCapsula", numero)
                i.putExtra("statusAtual", statusBonito)
                statusLauncher.launch(i)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        sincronizarComFirestore()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putStringArray("statusCapsulas", statusCapsulas.toTypedArray())
    }

    // --------- Firestore (Adm) ---------

    private fun docId(n: Int) = "capsula$n"

    /** Lê Firestore e reflete na UI (converte variações para o code interno) */
    private fun sincronizarComFirestore() {
        for (n in 1..8) {
            db.collection(CAPS).document(docId(n)).get()
                .addOnSuccessListener { snap ->
                    val disp = snap.getString("disponibilidade")
                    val code = when (disp?.normalizado()) {
                        "disponivel"     -> "DISPONIVEL"
                        "indisponivel"   -> "INDISPONIVEL"
                        "manutencao", "emmanutencao" -> "MANUTENCAO"
                        else -> "DISPONIVEL"
                    }
                    statusCapsulas[n] = code
                    aplicarStatusNaUI(n, code)
                }
                .addOnFailureListener {
                    // mantém estado anterior se falhar
                }
        }
    }

    /** Persiste no Firestore e atualiza UI local */
    private fun atualizarStatusNoFirestore(numero: Int, code: String) {
        val valor = when (code) {
            "DISPONIVEL"   -> CapsStatus.DISP
            "INDISPONIVEL" -> CapsStatus.INDISP
            "MANUTENCAO"   -> CapsStatus.MANUT   // grava "Em manutenção"
            else           -> CapsStatus.DISP
        }

        db.collection(CAPS).document(docId(numero))
            .update(mapOf("disponibilidade" to valor))
            .addOnSuccessListener {
                statusCapsulas[numero] = code
                aplicarStatusNaUI(numero, code)
                Toast.makeText(this, "Cápsula $numero atualizada para $valor.", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Erro ao atualizar: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
            }
    }

    // --------- Helpers de UI ---------

    private fun aplicarStatusNaUI(numero: Int, code: String) {
        // Botão (cor)
        val idBtn = when (numero) {
            1 -> R.id.btnCapsula1AdmCapsulasAdmSergio
            2 -> R.id.btnCapsula2AdmCapsulasAdmSergio
            3 -> R.id.btnCapsula3AdmCapsulasAdmSergio
            4 -> R.id.btnCapsula4AdmCapsulasAdmSergio
            5 -> R.id.btnCapsula5AdmCapsulasAdmSergio
            6 -> R.id.btnCapsula6AdmCapsulasAdmSergio
            7 -> R.id.btnCapsula7AdmCapsulasAdmSergio
            8 -> R.id.btnCapsula8AdmCapsulasAdmSergio
            else -> null
        }
        idBtn?.let { id ->
            val btn = findViewById<ImageButton>(id)
            when (code) {
                "DISPONIVEL"   -> btn.setColorFilter(getColor(android.R.color.holo_green_light))
                "INDISPONIVEL" -> btn.setColorFilter(getColor(android.R.color.holo_red_light))
                "MANUTENCAO"   -> btn.setColorFilter(getColor(android.R.color.holo_orange_light))
                else           -> btn.clearColorFilter()
            }
        }

        // Texto do status
        val idTxt = when (numero) {
            1 -> R.id.statusCapsula1AdmCapsulasAdmSergio
            2 -> R.id.statusCapsula2AdmCapsulasAdmSergio
            3 -> R.id.statusCapsula3AdmCapsulasAdmSergio
            4 -> R.id.statusCapsula4AdmCapsulasAdmSergio
            5 -> R.id.statusCapsula5AdmCapsulasAdmSergio
            6 -> R.id.statusCapsula6AdmCapsulasAdmSergio
            7 -> R.id.statusCapsula7AdmCapsulasAdmSergio
            8 -> R.id.statusCapsula8AdmCapsulasAdmSergio
            else -> null
        }
        idTxt?.let { id ->
            val tv = findViewById<TextView>(id)
            tv.maxLines = 2
            when (code) {
                "DISPONIVEL" -> { tv.text = "Status: Disponível";     tv.setTextColor(0xFF008000.toInt()) }
                "INDISPONIVEL" -> { tv.text = "Status: Indisponível"; tv.setTextColor(0xFFFF0000.toInt()) }
                "MANUTENCAO" -> { tv.text = "Status: Em manutenção";  tv.setTextColor(0xFFFFA500.toInt()) }
            }
        }
    }

    private fun String.normalizado(): String {
        val semAcento = Normalizer.normalize(this, Normalizer.Form.NFD)
            .replace("\\p{Mn}+".toRegex(), "")
        return semAcento.lowercase().replace("\\s+".toRegex(), "")
    }
}
