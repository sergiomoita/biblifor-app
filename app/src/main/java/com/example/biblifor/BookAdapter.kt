package com.example.biblifor

import Book
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BookAdapter(
    private val items: List<Book>,
    private val onItemClick: (Book) -> Unit
) : RecyclerView.Adapter<BookAdapter.VH>() {

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
        val book = items[position]

        holder.txtTitulo.text = book.title

        if (book.emprestavel) {
            holder.txtStatus.text = "Emprestável"
            holder.txtStatus.setTextColor(0xFF00C853.toInt()) // verde
        } else {
            holder.txtStatus.text = "Não-emprestável"
            holder.txtStatus.setTextColor(0xFFFF5252.toInt()) // vermelho
        }

        val bitmap = base64ToBitmap(book.imagemBase64)
        if (bitmap != null) {
            holder.imgCapa.setImageBitmap(bitmap)
        } else {
            holder.imgCapa.setImageResource(book.coverRes)
        }

        holder.itemView.setOnClickListener { onItemClick(book) }
    }

    override fun getItemCount(): Int = items.size

    private fun base64ToBitmap(base64: String?): Bitmap? {
        if (base64.isNullOrBlank()) return null
        return try {
            val bytes = Base64.decode(base64, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        } catch (_: Exception) {
            null
        }
    }
}
