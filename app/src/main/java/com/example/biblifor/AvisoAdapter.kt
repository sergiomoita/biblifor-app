package com.example.biblifor.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.biblifor.R
import com.example.biblifor.model.Aviso
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale

class AvisoAdapter(private val lista: List<Aviso>) :
    RecyclerView.Adapter<AvisoAdapter.AvisoViewHolder>() {

    class AvisoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtTitulo: TextView = itemView.findViewById(R.id.itemAvisoTitulo)
        val txtData: TextView = itemView.findViewById(R.id.itemAvisoData)
        val txtMensagem: TextView = itemView.findViewById(R.id.itemAvisoMensagem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AvisoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_aviso, parent, false)
        return AvisoViewHolder(view)
    }

    override fun getItemCount(): Int = lista.size

    override fun onBindViewHolder(holder: AvisoViewHolder, position: Int) {
        val aviso = lista[position]

        holder.txtTitulo.text = aviso.titulo
        holder.txtMensagem.text = aviso.mensagem

        // ====== Formatando o Timestamp ======
        aviso.data?.let {
            holder.txtData.text = formatarData(it)
        }
    }

    private fun formatarData(timestamp: Timestamp): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return sdf.format(timestamp.toDate())
    }
}
