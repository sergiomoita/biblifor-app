package com.example.biblifor

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.biblifor.adapter.AvisoAdapter
import com.example.biblifor.model.Aviso
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.ktx.firestore

class MenuPrincipalAdministradorActivity : BaseActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var rvUltimosEventos: RecyclerView
    private lateinit var adapterAvisos: AvisoAdapter
    private val listaUltimosAvisos = mutableListOf<Aviso>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu_principal_administrador)

        // ===== Ajuste de insets (barras do sistema) =====
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // ===== Inicializa Firestore =====
        db = Firebase.firestore

        // ===== Configuração da RecyclerView =====
        rvUltimosEventos = findViewById(R.id.rvUltimosEventos)
        rvUltimosEventos.layoutManager = LinearLayoutManager(this)
        adapterAvisos = AvisoAdapter(listaUltimosAvisos)
        rvUltimosEventos.adapter = adapterAvisos

        // ===== Recupera matrícula e nome do administrador logado =====
        val prefs = getSharedPreferences("APP_PREFS", MODE_PRIVATE)
        val matriculaAdm = prefs.getString("MATRICULA_ADM", null)
        val nomeAdm = prefs.getString("NOME_ADM", null)

        val tvOlaAdm = findViewById<TextView>(R.id.leoOlaAdministrador37)
        val tvMatriculaAdm = findViewById<TextView>(R.id.leoMatriculaAdministrador37)

        // Exibe no XML
        tvOlaAdm.text = if (!nomeAdm.isNullOrEmpty()) "Olá, $nomeAdm" else "Olá, Administrador"
        tvMatriculaAdm.text = matriculaAdm ?: ""

        // Carrega últimos avisos
        if (matriculaAdm != null) {
            carregarUltimosAvisos(matriculaAdm)
        } else {
            Log.e("ADM", "⚠️ Nenhuma matrícula de administrador encontrada")
        }


        // ===== PERFIL (foto -> abre perfil do administrador) =====
        findViewById<ImageView>(R.id.leoFotoAdministrador37).setOnClickListener {
            startActivity(Intent(this, PerfilAdministradorActivity::class.java))
        }

        // ===== ACESSIBILIDADE =====
        val botaoAcessibilidade = findViewById<ImageView>(R.id.leoBotaoAcessibilidadeAdm37)
        val textosParaAcessibilidade = listOf<TextView>(
            findViewById(R.id.leotextViewUniforAdministrador37),
            findViewById(R.id.leoOlaAdministrador37),
            findViewById(R.id.leoMatriculaAdministrador37),
            findViewById(R.id.leoTituloAcervoAdm37),
            findViewById(R.id.leoTituloCapsulasAdm37),
            findViewById(R.id.leoTituloEventosAdm37),
            findViewById(R.id.leoNovaMensagemAdm37)
        )

        val coresOriginais = textosParaAcessibilidade.map { it to it.currentTextColor }
        val corAcessivel = Color.parseColor("#FFFF00")
        var acessibilidadeAtiva = false

        botaoAcessibilidade.setOnClickListener {
            if (!acessibilidadeAtiva) {
                textosParaAcessibilidade.forEach { it.setTextColor(corAcessivel) }
            } else {
                coresOriginais.forEach { (view, color) -> view.setTextColor(color) }
            }
            acessibilidadeAtiva = !acessibilidadeAtiva
        }

        // ===== BOTÕES DE AÇÃO =====
        findViewById<ImageView>(R.id.leoBotaoEscreverMensagemAdm37).setOnClickListener {
            startActivity(Intent(this, EscreverMensagemAdministradorActivity::class.java))
        }

        findViewById<Button>(R.id.leoBotaoCadastrarAdm37).setOnClickListener {
            startActivity(Intent(this, CadastrarLivroAdministradorActivity::class.java))
        }

        findViewById<Button>(R.id.leoBotaoEmprestarAdm37).setOnClickListener {
            startActivity(Intent(this, LivrosEmprestaveisAdministradorActivity::class.java))
        }

        findViewById<TextView>(R.id.btnVerMaisCapsulas).setOnClickListener {
            startActivity(Intent(this, CapsulasAdministradorActivity::class.java))
        }

        // ===== SEÇÃO NOVA MENSAGEM =====
        val secaoNovaMensagem = findViewById<LinearLayout>(R.id.secaoNovaMensagem)
        val botaoNovaMensagem = findViewById<ImageView>(R.id.leoBotaoNovaMensagem237)
        val abrirNovaMensagem = Intent(this, EscreverMensagemAdministradorActivity::class.java)
        secaoNovaMensagem.setOnClickListener { startActivity(abrirNovaMensagem) }
        botaoNovaMensagem.setOnClickListener { startActivity(abrirNovaMensagem) }

        // ===== SEÇÕES CLICÁVEIS =====
        findViewById<LinearLayout>(R.id.secaoCapsulas).setOnClickListener {
            startActivity(Intent(this, CapsulasAdministradorActivity::class.java))
        }

        findViewById<LinearLayout>(R.id.secaoEventos).setOnClickListener {
            startActivity(Intent(this, MensagensAdministradorActivity::class.java))
        }

        findViewById<TextView>(R.id.btnVerMaisEventos).setOnClickListener {
            startActivity(Intent(this, MensagensAdministradorActivity::class.java))
        }

        // ===== BARRA INFERIOR FIXA =====
        findViewById<ImageView>(R.id.leoLogoHome3).setOnClickListener {
            startActivity(Intent(this, MenuPrincipalAdministradorActivity::class.java))
            finish()
        }

        findViewById<ImageView>(R.id.leoImagemChatbot3).setOnClickListener {
            startActivity(Intent(this, EscreverMensagemAdministradorActivity::class.java))
        }

        findViewById<ImageView>(R.id.leoImagemNotificacoes3).setOnClickListener {
            startActivity(Intent(this, MensagensAdministradorActivity::class.java))
        }

        findViewById<ImageView>(R.id.leoImagemMenu3).setOnClickListener {
            startActivity(Intent(this, MenuHamburguerAdministradorActivity::class.java))
        }
    }

    // ==== FUNÇÃO PARA CARREGAR OS 3 ÚLTIMOS AVISOS DO ADMINISTRADOR ====
    private fun carregarUltimosAvisos(matriculaAdm: String) {
        db.collection("mensagens")
            .whereEqualTo("matriculaAdm", matriculaAdm)
            .orderBy("data", Query.Direction.DESCENDING)
            .limit(3)
            .get()
            .addOnSuccessListener { result ->
                listaUltimosAvisos.clear()
                for (doc in result) {
                    val aviso = Aviso(
                        titulo = doc.getString("titulo") ?: "",
                        mensagem = doc.getString("mensagem") ?: "",
                        matricula = doc.getString("matricula") ?: "",
                        matriculaAdm = doc.getString("matriculaAdm") ?: "",
                        data = doc.getTimestamp("data")
                    )
                    listaUltimosAvisos.add(aviso)
                }
                adapterAvisos.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                Log.e("AVISOS_ADM", "❌ Erro ao carregar avisos do administrador: ${e.localizedMessage}")
            }
    }
}
