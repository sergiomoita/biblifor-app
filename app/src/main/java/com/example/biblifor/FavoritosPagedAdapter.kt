package com.example.biblifor

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FavoritosPagedAdapter(
    private val onItemClick: (Book) -> Unit
) : RecyclerView.Adapter<FavoritosPagedAdapter.VH>() {

    private val items = mutableListOf<Book>()

    class VH(v: View) : RecyclerView.ViewHolder(v) {
        val imgCapa: ImageView = v.findViewById(R.id.imgCapa)
        val txtTitulo: TextView = v.findViewById(R.id.txtTitulo)
        val txtStatus: TextView = v.findViewById(R.id.txtStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_livro_emprestimo, parent, false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val b = items[position]
        holder.imgCapa.setImageResource(b.coverRes)
        holder.txtTitulo.text = b.title

        if (b.emprestavel) {
            holder.txtStatus.text = "Emprestável"
            holder.txtStatus.setTextColor(Color.parseColor("#00C853"))
        } else {
            holder.txtStatus.text = "Não-emprestável"
            holder.txtStatus.setTextColor(Color.parseColor("#FF3B30"))
        }

        holder.itemView.setOnClickListener { onItemClick(b) }
    }

    override fun getItemCount(): Int = items.size

    fun submitPage(pageItems: List<Book>) {
        items.clear()
        items.addAll(pageItems)
        notifyDataSetChanged()
    }
}
