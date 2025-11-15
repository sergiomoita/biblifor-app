package com.example.biblifor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HistoryPagedAdapter(
    private val onClick: (HistoryBook) -> Unit
) : RecyclerView.Adapter<HistoryPagedAdapter.HistoryViewHolder>() {

    private val items = mutableListOf<HistoryBook>()

    fun submitPage(lista: List<HistoryBook>) {
        items.clear()
        items.addAll(lista)
        notifyDataSetChanged()
    }

    class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivCapa: ImageView = itemView.findViewById(R.id.ivCapaHistory)
        val tvTitulo: TextView = itemView.findViewById(R.id.tvTituloHistory)
        val tvAutor: TextView = itemView.findViewById(R.id.tvAutorHistory)
        val tvDisponibilidade: TextView = itemView.findViewById(R.id.tvDisponibilidadeHistory)
        val tvData: TextView = itemView.findViewById(R.id.tvDataHistory)
        val tvStatus: TextView = itemView.findViewById(R.id.tvStatusHistory)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_history_book, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val book = items[position]

        holder.ivCapa.setImageResource(book.coverRes)
        holder.tvTitulo.text = book.title
        holder.tvAutor.text = book.author
        holder.tvDisponibilidade.text = book.availabilityText
        holder.tvData.text = book.dateText

        if (book.statusText.isNullOrBlank()) {
            holder.tvStatus.visibility = View.GONE
        } else {
            holder.tvStatus.visibility = View.VISIBLE
            holder.tvStatus.text = book.statusText
        }

        holder.itemView.setOnClickListener {
            onClick(book)
        }
    }

    override fun getItemCount(): Int = items.size
}
