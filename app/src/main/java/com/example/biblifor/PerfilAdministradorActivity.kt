package com.example.biblifor

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PerfilAdministradorActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Altere para o nome real do layout da Activity do perfil do administrador
        setContentView(R.layout.activity_perfil_administrador)

        // 1) RecyclerView da tela do administrador (id: recyclerHistorico42)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerHistorico42)

        // 2) LayoutManager
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        // (Opcional) Linha divisória entre itens
        recyclerView.addItemDecoration(
            DividerItemDecoration(this, layoutManager.orientation)
        )

        // 3) Dados de exemplo (você pode substituir por dados reais)
        val historico = listOf(
            HistoricoEmprestimo("Mensagem de Teste  18/09/2025"),
            HistoricoEmprestimo("Tarde de Leitura  02/09/2025"),
            HistoricoEmprestimo("Oficina Escrita  28/08/2025"),
            HistoricoEmprestimo("Ajudar Alunos  19/08/2025"),
            HistoricoEmprestimo("Monitores Biblioteca  04/08/2025"),
            HistoricoEmprestimo("Como Consultar  04/08/2025")
        )

        // 4) Adapter reaproveitado
        recyclerView.adapter = HistoricoEmprestimoAdapter(historico)

        val leoBotaoVoltarPADM42 = findViewById<ImageView>(R.id.leoImagemSetaPADM42)
        leoBotaoVoltarPADM42.setOnClickListener {
            val navegarVoltarAdm42 = Intent (this, MenuPrincipalAdministradorActivity::class.java)
            startActivity(navegarVoltarAdm42)
        }
        val leoBotaoNotificacoesSPADM42 = findViewById<ImageView>(R.id.leoImagemNotificacaoSuperiorPADM42)
        leoBotaoNotificacoesSPADM42.setOnClickListener {
            val navegarNotificacoesSAdm42 = Intent (this, MensagensAdministradorActivity::class.java)
            startActivity(navegarNotificacoesSAdm42)
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