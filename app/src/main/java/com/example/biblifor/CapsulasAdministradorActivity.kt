package com.example.biblifor

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import java.text.Normalizer

class CapsulasAdministradorActivity : AppCompatActivity() {

    // Mantém o estado das 8 cápsulas (índice 1..8; índice 0 é descartado)
    // Valores possíveis: "DISPONIVEL", "INDISPONIVEL", "MANUTENCAO"
    private var statusCapsulas: MutableList<String> = MutableList(9) { "DISPONIVEL" }

    // ✅ Recebe o novo status e aplica no array + UI
    private val statusLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data ?: return@registerForActivityResult
                val numero = data.getIntExtra("numeroCapsula", -1)
                val novoStatus = (data.getStringExtra("novoStatus") ?: return@registerForActivityResult).normalizado()

                val code = when (novoStatus) {
                    "disponivel"   -> "DISPONIVEL"
                    "indisponivel" -> "INDISPONIVEL"
                    "manutencao"   -> "MANUTENCAO"
                    else           -> "DISPONIVEL"
                }

                if (numero in 1..8) {
                    statusCapsulas[numero] = code       // atualiza o estado “fonte da verdade”
                    aplicarStatusNaUI(numero, code)     // reflete imediatamente na UI
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_capsulas_administrador)

        // Restaura estado (em caso de rotação/recriação)
        if (savedInstanceState != null) {
            val arr = savedInstanceState.getStringArray("statusCapsulas")
            if (arr != null && arr.size == 9) {
                statusCapsulas = arr.toMutableList()
            }
        } else {
            // Primeira vez: lê o que está no layout e inicializa o array
            for (i in 1..8) {
                statusCapsulas[i] = lerStatusDoTextView(i)
            }
        }

        // Garante que a UI reflita o array sempre que a Activity abrir
        for (i in 1..8) aplicarStatusNaUI(i, statusCapsulas[i])

        // 🔙 Botão de voltar
        val btnVoltar = findViewById<ImageView>(R.id.btnVoltarCapsulasAdmSergio)
        btnVoltar.setOnClickListener {
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

        // 💊 Clique nas cápsulas: abre a tela já levando o status atual correto
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
                    "MANUTENCAO"   -> "Manutenção"
                    else           -> "Disponível"
                }
                val i = Intent(this, StatusCapsulaAdministradorActivity::class.java)
                i.putExtra("numeroCapsula", numero)
                i.putExtra("statusAtual", statusBonito)
                statusLauncher.launch(i)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putStringArray("statusCapsulas", statusCapsulas.toTypedArray())
    }

    // --------- Helpers ---------

    // Lê o TextView que já está no layout e retorna o code (DISPONIVEL/INDISPONIVEL/MANUTENCAO)
    private fun lerStatusDoTextView(numero: Int): String {
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
        } ?: return "DISPONIVEL"

        val txt = findViewById<TextView>(idTxt).text.toString().normalizado()
        return when {
            "indisponivel" in txt -> "INDISPONIVEL"
            "manutencao"  in txt -> "MANUTENCAO"
            else                  -> "DISPONIVEL"
        }
    }

    // Aplica cor do botão + texto do status para UMA cápsula
    private fun aplicarStatusNaUI(numero: Int, code: String) {
        // Botão
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

        // Texto
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
                "DISPONIVEL" -> {
                    tv.text = "Status: Disponível"
                    tv.setTextColor(0xFF008000.toInt())
                }
                "INDISPONIVEL" -> {
                    tv.text = "Status: Indisponível"
                    tv.setTextColor(0xFFFF0000.toInt())
                }
                "MANUTENCAO" -> {
                    tv.text = "Status: Manutenção"
                    tv.setTextColor(0xFFFFA500.toInt())
                }
            }
        }
    }

    private fun String.normalizado(): String {
        val semAcento = Normalizer.normalize(this, Normalizer.Form.NFD)
            .replace("\\p{Mn}+".toRegex(), "")
        return semAcento.lowercase()
    }
}
