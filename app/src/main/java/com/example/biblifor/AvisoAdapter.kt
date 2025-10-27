package com.example.biblifor.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.biblifor.R
import com.example.biblifor.model.Aviso

class AvisoAdapter(
    private val itens: List<Aviso>
) : RecyclerView.Adapter<AvisoAdapter.AvisoVH>() {

    class AvisoVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtOrigem: TextView = itemView.findViewById(R.id.txtOrigem)
        val txtData: TextView = itemView.findViewById(R.id.txtData)
        val txtMensagem: TextView = itemView.findViewById(R.id.txtMensagem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AvisoVH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_usuario_aviso, parent, false)
        return AvisoVH(view)
    }

    override fun onBindViewHolder(holder: AvisoVH, position: Int) {
        val aviso = itens[position]
        holder.txtOrigem.text = aviso.origem
        holder.txtData.text = aviso.data
        holder.txtMensagem.text = aviso.mensagem
    }

    override fun getItemCount(): Int = itens.size
}
