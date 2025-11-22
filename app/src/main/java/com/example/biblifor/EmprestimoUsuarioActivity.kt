package com.example.biblifor

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class EmprestimoUsuarioActivity : BaseActivity() {

    private var dataEscolhida: String = ""
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emprestimo_usuario)

        // =====================================================================
        // 1) DADOS VINDOS DO POPUP
        // =====================================================================
        val livroId = intent.getStringExtra("livroId") ?: ""
        val titulo = intent.getStringExtra("titulo") ?: ""
        val autor = intent.getStringExtra("autor") ?: ""
        val imagemBase64 = intent.getStringExtra("imagemBase64")
        val unidadesDisponiveis = intent.getIntExtra("unidades", 1)

        val prefs = getSharedPreferences("APP_PREFS", MODE_PRIVATE)
        val matricula = prefs.getString("MATRICULA_USER", "") ?: ""

        // =====================================================================
        // 2) VIEWS
        // =====================================================================
        val imgCapa = findViewById<ImageView>(R.id.imgCapaEmprestimo)
        val txtTitulo = findViewById<TextView>(R.id.txtTituloEmprestimo)
        val txtUnidades = findViewById<TextView>(R.id.txtUnidadesEmprestimo)
        val fieldDataDevolucao = findViewById<TextView>(R.id.fieldDataDevolucao)
        val btnAceitar = findViewById<Button>(R.id.lopesBtnAceitar23)
        val btnRecusar = findViewById<Button>(R.id.lopesBtnRecusar23)

        txtTitulo.text = if (autor.isNotEmpty()) "$titulo ($autor)" else titulo
        txtUnidades.text = "Unidades disponíveis: $unidadesDisponiveis"

        // =====================================================================
        // 3) CARREGAR IMAGEM BASE64
        // =====================================================================
        if (!imagemBase64.isNullOrBlank()) {
            try {
                val bytes = Base64.decode(imagemBase64, Base64.DEFAULT)
                val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                imgCapa.setImageBitmap(bmp)
            } catch (e: Exception) {
                imgCapa.setImageResource(R.drawable.livro_rachelqueiroz)
            }
        } else {
            imgCapa.setImageResource(R.drawable.livro_rachelqueiroz)
        }

        // =====================================================================
        // 4) BUSCAR LOCALIZAÇÃO CORRETA DO FIRESTORE (CodigoAcervo)
        // =====================================================================
        var localizacaoDoLivro = "Carregando..."

        if (livroId.isNotEmpty()) {
            db.collection("livros")
                .document(livroId)
                .get()
                .addOnSuccessListener { doc ->
                    val codigo = doc.getString("CodigoAcervo")
                    localizacaoDoLivro = if (!codigo.isNullOrEmpty()) codigo else "Não informado"
                }
                .addOnFailureListener {
                    localizacaoDoLivro = "Erro ao carregar"
                }
        }

        // =====================================================================
        // 5) DATE PICKER
        // =====================================================================
        fieldDataDevolucao.setOnClickListener {

            val c = Calendar.getInstance()
            val ano = c.get(Calendar.YEAR)
            val mes = c.get(Calendar.MONTH)
            val dia = c.get(Calendar.DAY_OF_MONTH)

            val dp = DatePickerDialog(
                this,
                { _, y, m, d ->
                    dataEscolhida = "%02d/%02d/%04d".format(d, m + 1, y)
                    fieldDataDevolucao.text = "Devolver em: $dataEscolhida"
                },
                ano, mes, dia
            )

            dp.datePicker.minDate = System.currentTimeMillis()
            dp.show()
        }

        // =====================================================================
        // 6) BOTÃO ACEITAR → SALVANDO E ENVIANDO PARA CONFIRMAÇÃO
        // =====================================================================
        btnAceitar.setOnClickListener {

            if (dataEscolhida.isEmpty()) {
                Toast.makeText(this, "Selecione a data de devolução!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val dataHoje = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())

            val dadosEmprestimo = mapOf(
                "nome" to titulo,
                "autor" to autor,
                "dataEmprestimo" to dataHoje,
                "dataDevolucao" to dataEscolhida,
                "status" to "Ativo",
                "livroId" to livroId,
                "localizacao" to localizacaoDoLivro
            )

            db.collection("alunos")
                .document(matricula)
                .collection("historicoEmprestimos")
                .document(livroId)
                .set(dadosEmprestimo)
                .addOnSuccessListener {

                    val intent = Intent(this, ConfirmacaoEmprestimoUsuarioActivity::class.java)
                    intent.putExtra("titulo", titulo)
                    intent.putExtra("imagemBase64", imagemBase64)
                    intent.putExtra("dataDevolucao", dataEscolhida)
                    intent.putExtra("livroId", livroId)
                    intent.putExtra("localizacao", localizacaoDoLivro)

                    startActivity(intent)
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Erro ao registrar empréstimo.", Toast.LENGTH_SHORT).show()
                }
        }

        // =====================================================================
        // 7) BOTÃO RECUSAR
        // =====================================================================
        btnRecusar.setOnClickListener { finish() }

        findViewById<ImageView>(R.id.lopesSetaVoltar23).setOnClickListener { finish() }

        // =====================================================================
        // 8) BARRA INFERIOR
        // =====================================================================
        findViewById<ImageView>(R.id.leoLogoHome3).setOnClickListener {
            startActivity(Intent(this, MenuPrincipalUsuarioActivity::class.java))
        }
        findViewById<ImageView>(R.id.leoImagemChatbot3).setOnClickListener {
            startActivity(Intent(this, ChatbotUsuarioActivity::class.java))
        }
        findViewById<ImageView>(R.id.leoImagemNotificacoes3).setOnClickListener {
            startActivity(Intent(this, AvisosUsuarioActivity::class.java))
        }
        findViewById<ImageView>(R.id.leoImagemMenu3).setOnClickListener {
            startActivity(Intent(this, MenuHamburguerUsuarioActivity::class.java))
        }

        // =====================================================================
        // 9) LISTA DE TERMOS (RESTAURADA)
        // =====================================================================
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerTermos23)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val termos = listOf(
            Termo("1. Cadastro — Apenas usuários devidamente cadastrados podem realizar empréstimos."),
            Termo("2. Prazo — Devolução em até 7 dias corridos."),
            Termo("3. Renovação — Permitida uma vez se não houver reserva."),
            Termo("4. Atrasos — Geram bloqueio temporário."),
            Termo("5. Danos — São de responsabilidade do usuário."),
            Termo("6. Reservas — Permitidas quando o livro estiver indisponível.")
        )

        recyclerView.adapter = TermosAdapter(termos)
    }
}
