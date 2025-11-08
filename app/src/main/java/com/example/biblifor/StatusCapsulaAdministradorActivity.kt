package com.example.biblifor

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class StatusCapsulaAdministradorActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_status_capsula_administrador)

        val numeroCapsula = intent.getIntExtra("numeroCapsula", -1)
        val statusAtual = intent.getStringExtra("statusAtual") ?: "Disponível" // ⬅️ recebido

        val btnVoltar = findViewById<ImageView>(R.id.btnVoltarStatusCapsulaAdministradorSergio)
        val txtTitulo = findViewById<TextView>(R.id.txtTituloCapsulaStatusCapsulaAdministradorSergio)
        val txtStatus = findViewById<TextView>(R.id.txtStatusCapsulaStatusCapsulaAdministradorSergio)

        val checkDisponivel = findViewById<CheckBox>(R.id.checkDisponivelStatusCapsulaAdministradorSergio)
        val checkIndisponivel = findViewById<CheckBox>(R.id.checkIndisponivelStatusCapsulaAdministradorSergio)
        val checkManutencao = findViewById<CheckBox>(R.id.checkManutencaoStatusCapsulaAdministradorSergio)

        if (numeroCapsula != -1) {
            txtTitulo.text = "Cápsula %02d".format(numeroCapsula)
        }

        // 🔹 Inicializa o texto e marca o checkbox conforme o status atual
        when (statusAtual.lowercase()) {
            "disponível", "disponivel" -> {
                txtStatus.text = "Status:\nDisponível"
                txtStatus.setTextColor(0xFF00C853.toInt())
                checkDisponivel.isChecked = true
            }
            "indisponível", "indisponivel" -> {
                txtStatus.text = "Status:\nIndisponível"
                txtStatus.setTextColor(0xFFFF0000.toInt())
                checkIndisponivel.isChecked = true
            }
            "manutenção", "manutencao" -> {
                txtStatus.text = "Status:\nManutenção"
                txtStatus.setTextColor(0xFFFFA500.toInt())
                checkManutencao.isChecked = true
            }
        }

        btnVoltar.setOnClickListener { finish() }

        val checkboxes = listOf(checkDisponivel, checkIndisponivel, checkManutencao)
        fun desmarcarOutros(marcado: CheckBox) {
            checkboxes.forEach { if (it != marcado) it.isChecked = false }
        }

        checkDisponivel.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                desmarcarOutros(checkDisponivel)
                enviarResultado("Disponível", numeroCapsula)
            }
        }
        checkIndisponivel.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                desmarcarOutros(checkIndisponivel)
                enviarResultado("Indisponível", numeroCapsula)
            }
        }
        checkManutencao.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                desmarcarOutros(checkManutencao)
                enviarResultado("Manutenção", numeroCapsula)
            }
        }
    }

    private fun enviarResultado(novoStatus: String, numeroCapsula: Int) {
        val intent = Intent().apply {
            putExtra("numeroCapsula", numeroCapsula)
            putExtra("novoStatus", novoStatus)
        }
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}
