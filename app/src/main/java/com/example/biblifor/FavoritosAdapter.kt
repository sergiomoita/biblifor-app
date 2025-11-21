package com.example.biblifor.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.biblifor.FavoritoItem
import com.example.biblifor.R

class FavoritosAdapter(
    private val lista: MutableList<FavoritoItem>,
    private val onItemClick: (FavoritoItem) -> Unit
) : RecyclerView.Adapter<FavoritosAdapter.VH>() {

    class VH(view: View) : RecyclerView.ViewHolder(view) {
        val titulo: TextView = view.findViewById(R.id.itemFavoritoTitulo)
        val disponibilidade: TextView = view.findViewById(R.id.itemFavoritoDisponibilidade)
        val icone: ImageView = view.findViewById(R.id.itemFavoritoIcone)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_favorito, parent, false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = lista[position]

        holder.titulo.text = item.titulo
        holder.disponibilidade.text = item.disponibilidade

        holder.itemView.setOnClickListener { onItemClick(item) }
    }

    override fun getItemCount(): Int = lista.size

    fun updateList(novaLista: List<FavoritoItem>) {
        lista.clear()
        lista.addAll(novaLista)
        notifyDataSetChanged()
    }
}
