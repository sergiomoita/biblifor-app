package com.example.biblifor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HistoricoEmprestimoAdapter(
    private val lista: MutableList<HistoricoEmprestimo> = mutableListOf()
) : RecyclerView.Adapter<HistoricoEmprestimoAdapter.HistoricoEmprestimoViewHolder>() {

    class HistoricoEmprestimoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titulo: TextView = view.findViewById(R.id.itemHistoricoTitulo)
        val data: TextView = view.findViewById(R.id.itemHistoricoData)
        val detalhes: TextView = view.findViewById(R.id.itemHistoricoDetalhes)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoricoEmprestimoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_historico, parent, false)
        return HistoricoEmprestimoViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoricoEmprestimoViewHolder, position: Int) {
        val item = lista[position]

        // item.texto vem no formato "Título - Autor  Data"
        // Vamos separar para preencher direitinho:
        val partes = item.texto.split("  ")

        val tituloAutor = partes.getOrNull(0) ?: "Título desconhecido"
        val data = partes.getOrNull(1) ?: ""

        // Separa título e autor
        val titulo = tituloAutor.substringBefore(" - ").trim()
        val autor = tituloAutor.substringAfter(" - ", "").trim()

        holder.titulo.text = titulo
        holder.data.text = data
        holder.detalhes.text = autor
    }

    override fun getItemCount(): Int = lista.size

    fun updateList(novaLista: List<HistoricoEmprestimo>) {
        lista.clear()
        lista.addAll(novaLista)
        notifyDataSetChanged()
    }
}
