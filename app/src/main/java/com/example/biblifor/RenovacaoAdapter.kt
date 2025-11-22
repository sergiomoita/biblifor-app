package com.example.biblifor.adapter

import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.biblifor.Emprestimo
import com.example.biblifor.R

class RenovacaoAdapter(
    private val lista: List<Emprestimo>,
    private val onItemClick: (Emprestimo) -> Unit
) : RecyclerView.Adapter<RenovacaoAdapter.VH>() {

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val capa: ImageView = itemView.findViewById(R.id.itemCapaLivroRenovacao)
        val titulo: TextView = itemView.findViewById(R.id.itemTituloRenovacao)
        val autor: TextView = itemView.findViewById(R.id.itemAutorRenovacao)
        val status: TextView = itemView.findViewById(R.id.itemStatusRenovacao)
        val dataDev: TextView = itemView.findViewById(R.id.itemDataFimRenovacao)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_disponivel_renovacao, parent, false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = lista[position]

        // ================== CAPA DO LIVRO ==================
        val img = item.imagemBase64

        img?.let {
            try {
                val bytes = Base64.decode(it, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                holder.capa.setImageBitmap(bitmap)
            } catch (e: Exception) {
                holder.capa.setImageResource(R.drawable.livro_1984) // placeholder oficial
            }
        } ?: run {
            holder.capa.setImageResource(R.drawable.livro_1984)
        }

        // ================== TEXTO ==================
        holder.titulo.text = item.nome
        holder.autor.text = item.autor
        holder.status.text = item.status
        holder.dataDev.text = "Devolução: ${item.dataDevolucao}"

        // ================== CLIQUE ==================
        holder.itemView.setOnClickListener {
            onItemClick(item)
        }
    }

    override fun getItemCount(): Int = lista.size
}
