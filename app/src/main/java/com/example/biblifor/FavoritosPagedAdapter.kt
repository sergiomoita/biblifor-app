package com.example.biblifor

import Book
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
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

        // === TÍTULO ===
        holder.txtTitulo.text = b.title

        // === STATUS DE EMPRÉSTIMO ===
        if (b.emprestavel) {
            holder.txtStatus.text = "Emprestável"
            holder.txtStatus.setTextColor(0xFF00C853.toInt())
        } else {
            holder.txtStatus.text = "Não-emprestável"
            holder.txtStatus.setTextColor(0xFFFF5252.toInt())
        }

        // === IMAGEM BASE64 OU PADRÃO ===
        val bitmap = base64ToBitmap(b.imagemBase64)
        if (bitmap != null) {
            holder.imgCapa.setImageBitmap(bitmap)
        } else {
            holder.imgCapa.setImageResource(b.coverRes)
        }

        // ================================
        //  🔥 ABRIR POPUP COM TODOS DADOS
        // ================================
        holder.itemView.setOnClickListener {
            val ctx = holder.itemView.context
            val intent = Intent(ctx, PopupResultadosUsuarioActivity::class.java)

            intent.putExtra("titulo", b.tituloOriginal)          // título sem autor
            intent.putExtra("autor", b.autor)
            intent.putExtra("imagemBase64", b.imagemBase64)
            intent.putExtra("situacao", b.situacaoEmprestimo)
            intent.putExtra("disponibilidade", b.disponibilidade)

            ctx.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = items.size

    fun submitPage(pageItems: List<Book>) {
        items.clear()
        items.addAll(pageItems)
        notifyDataSetChanged()
    }

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
