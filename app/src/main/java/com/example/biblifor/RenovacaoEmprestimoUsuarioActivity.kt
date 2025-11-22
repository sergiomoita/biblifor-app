package com.example.biblifor

import android.graphics.BitmapFactory
import android.util.Base64
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RenovacaoEmprestimoUsuarioActivity : BaseActivity() {

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_renovacao_emprestimo_usuario)

        // ------------------ RECEBENDO DADOS ------------------
        val emprestimoId = intent.getStringExtra("emprestimoId") ?: return
        val livroNome = intent.getStringExtra("nome") ?: ""
        val livroAutor = intent.getStringExtra("autor") ?: ""
        val dataEmp = intent.getStringExtra("dataEmprestimo") ?: ""
        val dataDev = intent.getStringExtra("dataDevolucao") ?: ""
        val livroId = intent.getStringExtra("livroId") ?: ""
        val imagemBase64 = intent.getStringExtra("imagemBase64")

        // ------------------ VIEWS ------------------
        val imgLivro = findViewById<ImageView>(R.id.lopesLivroRomeuJulieta33)
        val txtNome = findViewById<TextView>(R.id.lopesNomeRomeu33)
        val txtAutor = findViewById<TextView>(R.id.lopesAutorRomeu33)
        val txtDevAtual = findViewById<TextView>(R.id.lopesDataDevolucao33)
        val txtNovaDev = findViewById<TextView>(R.id.lopesNovaData33)

        // ------------------ CARREGAR CAPA ------------------
        if (!imagemBase64.isNullOrEmpty()) {
            try {
                val bytes = Base64.decode(imagemBase64, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                imgLivro.setImageBitmap(bitmap)
            } catch (e: Exception) {
                imgLivro.setImageResource(R.drawable.livro_1984)
            }
        } else {
            imgLivro.setImageResource(R.drawable.livro_1984)
        }

        // ------------------ EXIBIR INFORMAÇÕES ------------------
        txtNome.text = livroNome
        txtAutor.text = livroAutor
        txtDevAtual.text = "Data de devolução: $dataDev"

        // ------------------ CALCULAR NOVA DATA (+7 DIAS) ------------------
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))
        val calendario = Calendar.getInstance()

        val dataOriginal = sdf.parse(dataDev)
        calendario.time = dataOriginal!!

        calendario.add(Calendar.DAY_OF_MONTH, 7)
        val novaDataStr = sdf.format(calendario.time)

        txtNovaDev.text = "Nova devolução: $novaDataStr"

        // ------------------ TERMOS DA RENOVAÇÃO ------------------
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerTermos33)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val termos = listOf(
            Termo("1. Renovação disponível apenas quando faltarem 3 dias ou menos para o vencimento."),
            Termo("2. A renovação adiciona +7 dias ao prazo atual."),
            Termo("3. O livro não pode estar reservado por outro usuário."),
            Termo("4. O atraso invalida a renovação automática."),
            Termo("5. A devolução deve seguir as normas da biblioteca.")
        )

        recyclerView.adapter = TermosAdapter(termos)

        // ----------------------------------------------------------
        // 🔥 BOTÃO ACEITAR — ATUALIZAR FIRESTORE
        // ----------------------------------------------------------
        val btnAceitar = findViewById<Button>(R.id.lopesBtnAceitar33)
        btnAceitar.setOnClickListener {

            val prefs = getSharedPreferences("APP_PREFS", MODE_PRIVATE)
            val matricula = prefs.getString("MATRICULA_USER", null)

            if (matricula == null) {
                Toast.makeText(this, "Erro: usuário não encontrado.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val novoMapa = mapOf(
                "dataDevolucao" to novaDataStr,
                "status" to "Renovado"
            )

            db.collection("alunos")
                .document(matricula)
                .collection("historicoEmprestimos")
                .document(emprestimoId)
                .update(novoMapa)
                .addOnSuccessListener {

                    val i = Intent(this, RenovacaoConfirmadaUsuarioActivity::class.java)

                    // 🔥 AQUI ESTÁ A MÁGICA: reenviando a capa!
                    i.putExtra("imagemBase64", imagemBase64)
                    i.putExtra("nome", livroNome)
                    i.putExtra("autor", livroAutor)
                    i.putExtra("novaData", novaDataStr)

                    startActivity(i)
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Erro ao renovar empréstimo.", Toast.LENGTH_SHORT).show()
                }
        }


        // ----------------------------------------------------------
        // BOTÃO RECUSAR
        // ----------------------------------------------------------
        val btnRecusar = findViewById<Button>(R.id.lopesBtnRecusar33)
        btnRecusar.setOnClickListener {
            finish()
        }

        // ----------------------------------------------------------
        // SETA VOLTAR
        // ----------------------------------------------------------
        findViewById<ImageView>(R.id.lopesSetaVoltar33).setOnClickListener {
            finish()
        }

        // ----------------------------------------------------------
        // BARRA INFERIOR
        // ----------------------------------------------------------
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
    }
}
