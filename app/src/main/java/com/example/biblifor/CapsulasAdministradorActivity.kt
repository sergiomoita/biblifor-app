package com.example.biblifor

import android.content.Intent
import android.os.Bundle
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
        const val DISP = "Disponível"
        const val INDISP = "Indisponível"
        const val MANUT = "Manutenção"
    }

    // índice 0 não usa, então lista com 13 posições
    private var statusCapsulas: MutableList<String> = MutableList(13) { "DISPONIVEL" }

    private val statusLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data ?: return@registerForActivityResult
                val numero = data.getIntExtra("numeroCapsula", -1)
                val novoStatus = (data.getStringExtra("novoStatus") ?: return@registerForActivityResult).normalizado()

                val code = when (novoStatus) {
                    "disponivel" -> "DISPONIVEL"
                    "indisponivel" -> "INDISPONIVEL"
                    "manutencao", "emmanutencao" -> "MANUTENCAO"
                    else -> "DISPONIVEL"
                }

                if (numero in 1..12) {
                    atualizarStatusNoFirestore(numero, code)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_capsulas_administrador)

        if (savedInstanceState != null) {
            val arr = savedInstanceState.getStringArray("statusCapsulas")
            if (arr != null && arr.size == 13) statusCapsulas = arr.toMutableList()
        }

        findViewById<ImageView>(R.id.btnVoltarCapsulasAdmSergio).setOnClickListener {
            startActivity(Intent(this, MenuPrincipalAdministradorActivity::class.java))
            finish()
        }

        // BARRA INFERIOR
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

        // IDs dos botões das cápsulas
        val idsCapsulas = listOf(
            R.id.btnCapsula1AdmCapsulasAdmSergio,
            R.id.btnCapsula2AdmCapsulasAdmSergio,
            R.id.btnCapsula3AdmCapsulasAdmSergio,
            R.id.btnCapsula4AdmCapsulasAdmSergio,
            R.id.btnCapsula5AdmCapsulasAdmSergio,
            R.id.btnCapsula6AdmCapsulasAdmSergio,
            R.id.btnCapsula7AdmCapsulasAdmSergio,
            R.id.btnCapsula8AdmCapsulasAdmSergio,
            R.id.btnCapsula9AdmCapsulasAdmSergio,
            R.id.btnCapsula10AdmCapsulasAdmSergio,
            R.id.btnCapsula11AdmCapsulasAdmSergio,
            R.id.btnCapsula12AdmCapsulasAdmSergio
        )

        idsCapsulas.forEachIndexed { index, idBtn ->
            findViewById<ImageView>(idBtn).setOnClickListener {
                val numero = index + 1
                val statusBonito = when (statusCapsulas[numero]) {
                    "DISPONIVEL" -> "Disponível"
                    "INDISPONIVEL" -> "Indisponível"
                    "MANUTENCAO" -> "Manutenção"
                    else -> "Disponível"
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

    private fun docId(n: Int) = "capsula$n"

    private fun sincronizarComFirestore() {
        for (n in 1..12) {
            db.collection(CAPS).document(docId(n)).get()
                .addOnSuccessListener { snap ->
                    val disp = snap.getString("disponibilidade")
                    val code = when (disp?.normalizado()) {
                        "disponivel" -> "DISPONIVEL"
                        "indisponivel" -> "INDISPONIVEL"
                        "manutencao", "emmanutencao" -> "MANUTENCAO"
                        else -> "DISPONIVEL"
                    }

                    statusCapsulas[n] = code
                    aplicarStatusNaUI(n, code)
                }
        }
    }

    private fun atualizarStatusNoFirestore(numero: Int, code: String) {
        val valor = when (code) {
            "DISPONIVEL" -> CapsStatus.DISP
            "INDISPONIVEL" -> CapsStatus.INDISP
            "MANUTENCAO" -> CapsStatus.MANUT
            else -> CapsStatus.DISP
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

    private fun aplicarStatusNaUI(numero: Int, code: String) {

        // BUSCA O BOTÃO (ÍCONE DE EDITAR)
        val idBtn = when (numero) {
            1 -> R.id.btnCapsula1AdmCapsulasAdmSergio
            2 -> R.id.btnCapsula2AdmCapsulasAdmSergio
            3 -> R.id.btnCapsula3AdmCapsulasAdmSergio
            4 -> R.id.btnCapsula4AdmCapsulasAdmSergio
            5 -> R.id.btnCapsula5AdmCapsulasAdmSergio
            6 -> R.id.btnCapsula6AdmCapsulasAdmSergio
            7 -> R.id.btnCapsula7AdmCapsulasAdmSergio
            8 -> R.id.btnCapsula8AdmCapsulasAdmSergio
            9 -> R.id.btnCapsula9AdmCapsulasAdmSergio
            10 -> R.id.btnCapsula10AdmCapsulasAdmSergio
            11 -> R.id.btnCapsula11AdmCapsulasAdmSergio
            12 -> R.id.btnCapsula12AdmCapsulasAdmSergio
            else -> null
        }

        // ÍCONE DE EDITAR SEMPRE BRANCO
        idBtn?.let { id ->
            val btn = findViewById<ImageView>(id)
            btn.clearColorFilter() // remove cor antiga
            btn.setColorFilter(getColor(android.R.color.white)) // deixa sempre branco
        }

        // BUSCA O TEXTO DO STATUS
        val idTxt = when (numero) {
            1 -> R.id.statusCapsula1AdmCapsulasAdmSergio
            2 -> R.id.statusCapsula2AdmCapsulasAdmSergio
            3 -> R.id.statusCapsula3AdmCapsulasAdmSergio
            4 -> R.id.statusCapsula4AdmCapsulasAdmSergio
            5 -> R.id.statusCapsula5AdmCapsulasAdmSergio
            6 -> R.id.statusCapsula6AdmCapsulasAdmSergio
            7 -> R.id.statusCapsula7AdmCapsulasAdmSergio
            8 -> R.id.statusCapsula8AdmCapsulasAdmSergio
            9 -> R.id.statusCapsula9AdmCapsulasAdmSergio
            10 -> R.id.statusCapsula10AdmCapsulasAdmSergio
            11 -> R.id.statusCapsula11AdmCapsulasAdmSergio
            12 -> R.id.statusCapsula12AdmCapsulasAdmSergio
            else -> null
        }

        // APLICA COR + EXPANDE SE NECESSÁRIO
        idTxt?.let { id ->
            val tv = findViewById<TextView>(id)
            tv.maxLines = 2
            tv.isSingleLine = false

            when (code) {
                "DISPONIVEL" -> {
                    tv.text = "Disponível"
                    tv.setTextColor(0xFF008000.toInt())
                }
                "INDISPONIVEL" -> {
                    tv.text = "Indisponível"
                    tv.setTextColor(0xFFFF0000.toInt())
                }
                "MANUTENCAO" -> {
                    tv.text = "Manutenção"
                    tv.setTextColor(0xFFFFA500.toInt())
                }
            }
        }
    }

    private fun String.normalizado(): String {
        val semAcento = Normalizer.normalize(this, Normalizer.Form.NFD)
            .replace("\\p{Mn}+".toRegex(), "")
        return semAcento.lowercase().replace("\\s+".toRegex(), "")
    }
}
