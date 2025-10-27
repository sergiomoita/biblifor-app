package com.example.biblifor

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HistoricoEmprestimoAdapter(private val lista: List<HistoricoEmprestimo>) :
    RecyclerView.Adapter<HistoricoEmprestimoAdapter.HistoricoEmprestimoViewHolder>() {

    class HistoricoEmprestimoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textoHistorico: TextView = view.findViewById(R.id.textoHistorico)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoricoEmprestimoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_historico, parent, false)
        return HistoricoEmprestimoViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoricoEmprestimoViewHolder, position: Int) {
        val item = lista[position]
        holder.textoHistorico.text = item.texto

        // 🔹 Quando o livro "Dom Casmurro" for clicado → abre PopUpHistoricoEmprestimoActivity
        holder.itemView.setOnClickListener {
            if (item.texto.contains("Dom Casmurro", ignoreCase = true)) {
                val intent = Intent(holder.itemView.context, PopupHistoricoEmprestimoUsuarioActivity::class.java)
                holder.itemView.context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int = lista.size
}
