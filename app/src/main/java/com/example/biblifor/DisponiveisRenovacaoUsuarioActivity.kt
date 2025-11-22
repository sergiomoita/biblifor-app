package com.example.biblifor

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.biblifor.adapter.RenovacaoAdapter
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Locale

class DisponiveisRenovacaoUsuarioActivity : BaseActivity() {

    private lateinit var rv: RecyclerView
    private lateinit var adapter: RenovacaoAdapter
    private val listaRenovaveis = mutableListOf<Emprestimo>()

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_disponiveis_renovacao_usuario)

        // ====== CONFIGURAÇÃO DO RECYCLERVIEW ======
        rv = findViewById(R.id.rvDisponiveisRenovacao)
        rv.layoutManager = LinearLayoutManager(this)

        adapter = RenovacaoAdapter(listaRenovaveis) { emprestimo ->
            abrirTelaRenovacao(emprestimo)
        }
        rv.adapter = adapter


        // ====== SETA VOLTAR ======
        findViewById<ImageView>(R.id.lopesSetaVoltar32).setOnClickListener {
            finish()
        }

        // ====== BARRA INFERIOR ======
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

        // ====== BUSCAR RENOVAÇÕES ======
        carregarLivrosDisponiveisRenovacao()
    }


    // ============================================================================
    // 🔥 ABRE TELA DE RENOVAÇÃO
    // ============================================================================
    private fun abrirTelaRenovacao(emp: Emprestimo) {
        val i = Intent(this, RenovacaoEmprestimoUsuarioActivity::class.java)

        i.putExtra("emprestimoId", emp.idDocumento)
        i.putExtra("nome", emp.nome)
        i.putExtra("autor", emp.autor)
        i.putExtra("dataEmprestimo", emp.dataEmprestimo)
        i.putExtra("dataDevolucao", emp.dataDevolucao)
        i.putExtra("livroId", emp.livroId)
        i.putExtra("imagemBase64", emp.imagemBase64)

        startActivity(i)
    }


    // ============================================================================
    // 🔥 CARREGA EMPRÉSTIMOS RENOVÁVEIS E A CAPA DO LIVRO
    // ============================================================================
    private fun carregarLivrosDisponiveisRenovacao() {
        val prefs = getSharedPreferences("APP_PREFS", MODE_PRIVATE)
        val matricula = prefs.getString("MATRICULA_USER", null) ?: return

        listaRenovaveis.clear()

        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))
        val hojeMillis = System.currentTimeMillis()
        val tresDiasMillis = 3L * 24 * 60 * 60 * 1000

        db.collection("alunos")
            .document(matricula)
            .collection("historicoEmprestimos")
            .get()
            .addOnSuccessListener { docs ->

                for (doc in docs) {

                    val status = doc.getString("status") ?: continue
                    if (status != "Ativo") continue

                    val nome = doc.getString("nome") ?: ""
                    val autor = doc.getString("autor") ?: ""
                    val dataDevString = doc.getString("dataDevolucao") ?: continue
                    val dataEmpString = doc.getString("dataEmprestimo") ?: ""
                    val livroId = doc.getString("livroId") ?: ""

                    val dataDevDate = try { sdf.parse(dataDevString) } catch (e: Exception) { null }
                    if (dataDevDate == null) continue

                    val dataDevMillis = dataDevDate.time

                    // regra de renovação: faltam 3 dias ou menos
                    if (hojeMillis >= dataDevMillis - tresDiasMillis) {

                        // Primeiro criamos o empréstimo sem a imagem
                        val emp = Emprestimo(
                            nome = nome,
                            autor = autor,
                            dataEmprestimo = dataEmpString,
                            dataDevolucao = dataDevString,
                            status = status,
                            livroId = livroId,
                            localizacao = doc.getString("localizacao") ?: "",
                            idDocumento = doc.id
                        )

                        // Agora buscamos a capa do livro na coleção "livros"
                        db.collection("livros")
                            .document(livroId)
                            .get()
                            .addOnSuccessListener { livroDoc ->

                                val capaBase64 = livroDoc.getString("Imagem")

                                // atualiza o objeto com a capa real
                                val empComCapa = emp.copy(imagemBase64 = capaBase64)

                                listaRenovaveis.add(empComCapa)
                                adapter.notifyDataSetChanged()
                            }
                    }
                }
            }
    }
}
