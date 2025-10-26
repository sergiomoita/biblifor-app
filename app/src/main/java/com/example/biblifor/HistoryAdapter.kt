package com.example.biblifor

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HistoryAdapter(
    private val items: List<HistoryBook>,
    private val onItemClick: ((HistoryBook) -> Unit)? = null
) : RecyclerView.Adapter<HistoryAdapter.VH>() {

    class VH(v: View) : RecyclerView.ViewHolder(v) {
        val imgCapa: ImageView = v.findViewById(R.id.imgCapaHist)
        val txtTitulo: TextView = v.findViewById(R.id.txtTituloHist)
        val txtAutor: TextView = v.findViewById(R.id.txtAutorHist)
        val txtDispon: TextView = v.findViewById(R.id.txtDisponHist)
        val txtStatus: TextView = v.findViewById(R.id.txtStatusHist)
        val txtData: TextView = v.findViewById(R.id.txtDataHist)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_historico_livro, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(h: VH, position: Int) {
        val it = items[position]
        h.imgCapa.setImageResource(it.coverRes)
        h.txtTitulo.text = it.title
        h.txtAutor.text = it.author
        h.txtDispon.text = it.availabilityText

        // Define cor da disponibilidade
        h.txtDispon.setTextColor(
            if (it.isAvailable) Color.parseColor("#00C853") else Color.parseColor("#FF3B30")
        )

        // Status opcional
        h.txtStatus.text = it.statusText ?: ""
        h.txtStatus.visibility = if (it.statusText.isNullOrBlank()) View.GONE else View.VISIBLE

        // Data
        h.txtData.text = it.dateText

        // Clique no item
        h.itemView.setOnClickListener {
            onItemClick?.invoke(items[position])
        }
    }

    override fun getItemCount(): Int = items.size
}
