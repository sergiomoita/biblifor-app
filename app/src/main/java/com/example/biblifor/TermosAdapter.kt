package com.example.biblifor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TermosAdapter(private val lista: List<Termo>) :
    RecyclerView.Adapter<TermosAdapter.TermoViewHolder>() {

    class TermoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textoTermo: TextView = view.findViewById(R.id.textoTermo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TermoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_termo, parent, false)
        return TermoViewHolder(view)
    }

    override fun onBindViewHolder(holder: TermoViewHolder, position: Int) {
        holder.textoTermo.text = lista[position].texto
    }

    override fun getItemCount(): Int = lista.size
}
