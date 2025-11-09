package com.example.biblifor

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class PerfilAdministradorActivity : BaseActivity() {

    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil_administrador)

        db = FirebaseFirestore.getInstance()

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerHistorico42)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.addItemDecoration(DividerItemDecoration(this, layoutManager.orientation))

        val historico = listOf(
            HistoricoEmprestimo("Mensagem de Teste 18/09/2025"),
            HistoricoEmprestimo("Tarde de Leitura 02/09/2025"),
            HistoricoEmprestimo("Oficina Escrita 28/08/2025"),
            HistoricoEmprestimo("Ajudar Alunos 19/08/2025"),
            HistoricoEmprestimo("Monitores Biblioteca 04/08/2025"),
            HistoricoEmprestimo("Como Consultar 04/08/2025")
        )
        recyclerView.adapter = HistoricoEmprestimoAdapter(historico)

        // Referências aos TextViews do layout
        val nomeAdm = findViewById<TextView>(R.id.leoNomeCompletoAdmPADM42)
        val funcaoAdm = findViewById<TextView>(R.id.leoNomeFuncaoAdmPADM42)
        val matriculaAdm = findViewById<TextView>(R.id.leoMatriculaAdmPADM42)

        db.collection("administrador").document("123").get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    nomeAdm.text = document.getString("nome") ?: "Nome não encontrado"
                    funcaoAdm.text = document.getString("cargo") ?: "Cargo não encontrado"
                    matriculaAdm.text = document.getString("matricula") ?: "Sem matrícula"
                } else {
                    nomeAdm.text = "Erro ao carregar"
                }
            }
            .addOnFailureListener {
                nomeAdm.text = "Falha ao conectar ao Firestore"
            }

        // 🔙 Botão de voltar
        findViewById<ImageView>(R.id.leoImagemSetaPADM42).setOnClickListener {
            startActivity(Intent(this, MenuPrincipalAdministradorActivity::class.java))
        }

        // 🔔 Notificações
        findViewById<ImageView>(R.id.leoImagemNotificacaoSuperiorPADM42).setOnClickListener {
            startActivity(Intent(this, MensagensAdministradorActivity::class.java))
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
    }
}
