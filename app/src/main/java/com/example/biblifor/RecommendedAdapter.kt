package com.example.biblifor

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.graphics.BitmapFactory
import android.util.Base64

class RecommendedAdapter(
    private val items: List<RecommendedBook>,
    private val onItemClick: ((RecommendedBook) -> Unit)? = null
) : RecyclerView.Adapter<RecommendedAdapter.VH>() {

    class VH(v: View) : RecyclerView.ViewHolder(v) {
        val imgCapa: ImageView = v.findViewById(R.id.imgCapaRec)
        val txtTitulo: TextView = v.findViewById(R.id.txtTituloRec)
        val txtDisp: TextView = v.findViewById(R.id.txtDisponibilidadeRec)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recomendado, parent, false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val b = items[position]

        // =======================
        // IMAGEM BASE64 OU FALLBACK
        // =======================
        if (!b.imagemBase64.isNullOrEmpty()) {
            try {
                val bytes = Base64.decode(b.imagemBase64, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                holder.imgCapa.setImageBitmap(bitmap)
            } catch (e: Exception) {
                holder.imgCapa.setImageResource(b.coverRes)
            }
        } else {
            holder.imgCapa.setImageResource(b.coverRes)
        }

        // =======================
        // TÍTULO
        // =======================
        holder.txtTitulo.text = b.title

        // =======================
        // DISPONIBILIDADE
        // =======================
        holder.txtDisp.text = b.availabilityText
        holder.txtDisp.setTextColor(
            if (b.available) Color.parseColor("#00C853")
            else Color.parseColor("#FF3B30")
        )

        // =======================
        // CLIQUE → envia o livro inteiro (com livroId!)
        // =======================
        holder.itemView.setOnClickListener { onItemClick?.invoke(b) }
    }

    override fun getItemCount(): Int = items.size
}
