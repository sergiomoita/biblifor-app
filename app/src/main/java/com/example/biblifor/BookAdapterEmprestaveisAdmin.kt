import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import Book
import com.example.biblifor.R
import com.example.biblifor.util.base64ToBitmap

class BookAdapterEmprestaveisAdmin(
    private val items: List<Book>,
    private val onItemClick: (Book) -> Unit
) : RecyclerView.Adapter<BookAdapterEmprestaveisAdmin.VH>() {

    class VH(v: View) : RecyclerView.ViewHolder(v) {
        val imgCapa: ImageView = v.findViewById(R.id.imgCapa)
        val txtTitulo: TextView = v.findViewById(R.id.txtTitulo)
        val txtStatus: TextView = v.findViewById(R.id.txtStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_livro_emprestimo_admin, parent, false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val b = items[position]

        // FOTO
        if (!b.imagemBase64.isNullOrEmpty()) {
            holder.imgCapa.setImageBitmap(base64ToBitmap(b.imagemBase64))
        }

        // TITULO
        holder.txtTitulo.text = b.title

        // STATUS
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
}
