package com.example.biblifor.adapter

import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.biblifor.Book
import com.example.biblifor.PopupResultadosUsuarioActivity
import com.example.biblifor.R

class FavoritosPagedAdapter(
    private val onItemClick: (Book) -> Unit
) : RecyclerView.Adapter<FavoritosPagedAdapter.VH>() {

    private val items = mutableListOf<Book>()

    class VH(view: View) : RecyclerView.ViewHolder(view) {
        val imgCapa: ImageView = view.findViewById(R.id.imgCapa)
        val txtTitulo: TextView = view.findViewById(R.id.txtTitulo)
        val txtStatus: TextView = view.findViewById(R.id.txtStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_livro_emprestimo, parent, false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val livro = items[position]

        // ----- TÍTULO -----
        holder.txtTitulo.text = livro.title

        // ----- STATUS / DISPONIBILIDADE -----
        holder.txtStatus.text = livro.disponibilidade
        holder.txtStatus.setTextColor(0xFFFFFFFF.toInt())

        // ----- CAPA -----
        if (!livro.imagemBase64.isNullOrEmpty()) {
            try {
                val bytes = Base64.decode(livro.imagemBase64, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                holder.imgCapa.setImageBitmap(bitmap)
            } catch (e: Exception) {
                holder.imgCapa.setImageResource(R.drawable.livro_1984)
            }
        } else {
            holder.imgCapa.setImageResource(R.drawable.livro_1984)
        }

        // ---- CLICK → abrir popup ----
        holder.itemView.setOnClickListener {
            val ctx = holder.itemView.context
            val intent = Intent(ctx, PopupResultadosUsuarioActivity::class.java)

            intent.putExtra("livroId", livro.livroId)
            intent.putExtra("titulo", livro.tituloOriginal)
            intent.putExtra("autor", livro.autor)
            intent.putExtra("imagemBase64", livro.imagemBase64)
            intent.putExtra("situacao", livro.situacaoEmprestimo)
            intent.putExtra("disponibilidade", livro.disponibilidade)

            ctx.startActivity(intent)
        }
    }

    override fun getItemCount() = items.size

    fun submitPage(novos: List<Book>) {
        items.clear()
        items.addAll(novos)
        notifyDataSetChanged()
    }
}
