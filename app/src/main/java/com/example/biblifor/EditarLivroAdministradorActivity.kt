package com.example.biblifor

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class EditarLivroAdministradorActivity : BaseActivity() {

    private fun validarCampo(et: EditText, label: String) {
        val valor = et.text.toString()
        if (valor.isBlank()) {
            throw IllegalArgumentException("$label não pode estar vazio.")
        }
        if (valor.contains("erro", ignoreCase = true)) {
            throw IllegalArgumentException("$label não pode conter 'erro'.")
        }
    }

    private fun showErrorToast(message: String) {
        val inflater: LayoutInflater = layoutInflater
        val view = inflater.inflate(R.layout.toast_excecao_cadastro, null)
        view.findViewById<TextView>(R.id.toast_message).text = message

        Toast(this).apply {
            duration = Toast.LENGTH_LONG
            setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 120)
            this.view = view
            show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_editar_livro_administrador)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 🔙 Ícones superiores
        findViewById<ImageView>(R.id.lopesSetaVoltar39).setOnClickListener {
            startActivity(Intent(this, MenuPrincipalAdministradorActivity::class.java))
        }
        findViewById<ImageView>(R.id.lopesEscrever46).setOnClickListener {
            startActivity(Intent(this, EscreverMensagemAdministradorActivity::class.java))
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
            startActivity(Intent(this, MenuPrincipalAdministradorActivity::class.java)); finish()
        }

        // ✏️ Campos editáveis
        val etNome = findViewById<EditText>(R.id.lopesNome46)
        val etAutor = findViewById<EditText>(R.id.lopesAutor46)
        val etTopicos = findViewById<EditText>(R.id.lopesTopicos46)
        val etQtd = findViewById<EditText>(R.id.lopesQtd46)
        val etLocaliza = findViewById<EditText>(R.id.lopesLocaliza46)

        // ====== Função auxiliar: botão alternante ======
        fun configurarBotaoAlternante(botao: Button, textoBase: String) {
            var estado = 0 // 0 = ✓ vermelho | 1 = ✓ verde | 2 = X vermelho

            botao.text = "✓ $textoBase"
            botao.setTextColor(Color.parseColor("#FF0000"))

            botao.setOnClickListener {
                estado = (estado + 1) % 3
                when (estado) {
                    0 -> { botao.text = "✓ $textoBase"; botao.setTextColor(Color.parseColor("#FF0000")) }
                    1 -> { botao.text = "✓ $textoBase"; botao.setTextColor(Color.parseColor("#00C853")) }
                    2 -> { botao.text = "X $textoBase"; botao.setTextColor(Color.parseColor("#FF0000")) }
                }
            }
        }

        // ====== Botões de disponibilidade e emprestar ======
        val btnFisico = findViewById<Button>(R.id.btnDispoFisico)
        val btnOnline = findViewById<Button>(R.id.btnDispoOnline)
        val btnEmpSim = findViewById<Button>(R.id.btnEmprestarSim)
        val btnEmpNao = findViewById<Button>(R.id.btnEmprestarNao)

        configurarBotaoAlternante(btnFisico, "Físico")
        configurarBotaoAlternante(btnOnline, "Online")
        configurarBotaoAlternante(btnEmpSim, "Sim")
        configurarBotaoAlternante(btnEmpNao, "Não")

        // ====== Botão Atualizar ======
        val btnAtualizar = findViewById<Button>(R.id.lopesBtnCadastrar46)
        btnAtualizar.setOnClickListener {
            try {
                validarCampo(etNome, "Nome")
                validarCampo(etAutor, "Autor")
                validarCampo(etTopicos, "Tópicos")
                validarCampo(etQtd, "Quantidade de exemplares")
                validarCampo(etLocaliza, "Localização no acervo")

                Toast.makeText(this, "Livro atualizado", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MenuPrincipalAdministradorActivity::class.java))
                finish()
            } catch (e: IllegalArgumentException) {
                showErrorToast(e.message ?: "Erro de validação.")
            } catch (e: Exception) {
                showErrorToast("Erro inesperado.")
            }
        }
    }
}
