package com.example.biblifor

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.launch

class ChatbotUsuarioActivity : BaseActivity() {

    // === Gemini (mantido) ===
    private lateinit var generativeModel: GenerativeModel
    private var prePrompt: String =
        "prePrompt = \"\"\"\n" +
                "VOCÊ É: Assistente da Biblioteca Unifor integrado ao app Biblifor.\n" +
                "TOM: educado, formal (sem soar frio), claro, paciente e “gente boa”.\n" +
                "IDIOMA: SEMPRE responda em português do Brasil.\n" +
                "PERSONA: Ajuda com uso do aplicativo, empréstimo/devolução, prazos, multas, reservas, pesquisa de acervo e serviços de apoio acadêmico. \n" +
                "PRIVACIDADE E SEGURANÇA:\n" +
                "- Nunca solicite senha do usuário.\n" +
                "- Nunca peça RA/matrícula completo; se precisar citar, diga para o usuário usá-lo apenas nos campos próprios do app.\n" +
                "- Não invente políticas. Se tiver dúvida, diga que não tem essa informação e oriente a procurar o balcão da biblioteca ou o suporte no app.\n" +
                "\n" +
                "FORMATAÇÃO:\n" +
                "- Responda em 1–3 linhas de texto sempre.\n" +
                "- Use listas apenas quando pedir “passo a passo”.\n" +
                "- Não inclua código, links externos ou informações não confirmadas.\n" +
                "\n" +
                "REGRAS DE FAQ… etc.\n" +
                "\"\"\"\n"

    // Chat
    private lateinit var rv: RecyclerView
    private lateinit var etPrompt: EditText
    private lateinit var btnSend: ImageView
    private val adapter by lazy { ChatAdapter() }
    private var pendingAiIndex: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 🔥 Mantém o input SEMPRE visível ao abrir o teclado
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        enableEdgeToEdge()
        setContentView(R.layout.activity_chatbot_usuario)

        // 🔥 Ajuste correto de insets para não esconder o input
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val imeHeight = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom
            v.setPadding(0, 0, 0, imeHeight)
            insets
        }

        // ---- Navegação existente ----
        findViewById<ImageView>(R.id.leoImagemSetaVoltarChatbot7).setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        findViewById<ImageView>(R.id.leoLogoHomeChatbotBF7).setOnClickListener {
            startActivity(Intent(this, MenuPrincipalUsuarioActivity::class.java))
        }
        findViewById<ImageView>(R.id.leoImagemChatbotBF7).setOnClickListener {
            startActivity(Intent(this, ChatbotUsuarioActivity::class.java))
        }
        findViewById<ImageView>(R.id.leoImagemNotificacoesChatbotBF7).setOnClickListener {
            startActivity(Intent(this, AvisosUsuarioActivity::class.java))
        }
        findViewById<ImageView>(R.id.leoImagemMenuChatbotBF7).setOnClickListener {
            startActivity(Intent(this, MenuPrincipalUsuarioActivity::class.java))
        }

        // ---- Views da conversa ----
        rv = findViewById(R.id.rvChat)
        rv.layoutManager = LinearLayoutManager(this).apply { stackFromEnd = true }
        rv.adapter = adapter

        etPrompt = findViewById(R.id.leoCampoEnviarMensagemChatbot7)
        btnSend = findViewById(R.id.leoBotaoEnviarMensagemChatbot7)

        // === Gemini (mantido) ===
        generativeModel = GenerativeModel(
            modelName = "gemini-2.5-flash",
            apiKey = "AIzaSyBuyxGLQ2ENPH_e8m0Q06pRPNHKaNIQgsk"
        )

        btnSend.setOnClickListener {
            val userText = etPrompt.text.toString().trim()
            if (userText.isEmpty()) {
                Toast.makeText(this, "Digite sua pergunta…", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Usuário → direita
            adapter.addMessage(ChatMsg(userText, fromAi = false))
            etPrompt.setText("")
            scrollToBottom()

            // IA → adiciona “Pensando…”
            pendingAiIndex = adapter.addMessageReturningIndex(
                ChatMsg("Pensando...", fromAi = true)
            )
            scrollToBottom()

            btnSend.isEnabled = false

            lifecycleScope.launch {
                try {
                    val response = generativeModel.generateContent(prePrompt + userText)
                    val txt = response.text ?: "Sem resposta do modelo."
                    pendingAiIndex?.let { adapter.updateMessage(it, txt) }
                } catch (e: Exception) {
                    pendingAiIndex?.let { adapter.updateMessage(it, "Erro ao gerar resposta.") }
                    Toast.makeText(
                        this@ChatbotUsuarioActivity,
                        "Falha: ${e.localizedMessage}",
                        Toast.LENGTH_SHORT
                    ).show()
                } finally {
                    pendingAiIndex = null
                    btnSend.isEnabled = true
                    scrollToBottom()
                }
            }
        }
    }

    private fun scrollToBottom() {
        rv.post { rv.scrollToPosition(adapter.itemCount - 1) }
    }

    // ================== Adapter ==================
    data class ChatMsg(val text: String, val fromAi: Boolean)

    private class ChatAdapter : RecyclerView.Adapter<ChatAdapter.VH>() {
        private val items = mutableListOf<ChatMsg>()

        fun addMessage(m: ChatMsg) {
            items += m
            notifyItemInserted(items.lastIndex)
        }

        fun addMessageReturningIndex(m: ChatMsg): Int {
            items += m
            notifyItemInserted(items.lastIndex)
            return items.lastIndex
        }

        fun updateMessage(index: Int, newText: String) {
            if (index in items.indices) {
                items[index] = items[index].copy(text = newText)
                notifyItemChanged(index)
            }
        }

        override fun getItemCount() = items.size
        override fun getItemViewType(position: Int) = if (items[position].fromAi) 1 else 0

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
            val container = androidx.appcompat.widget.LinearLayoutCompat(parent.context).apply {
                layoutParams = RecyclerView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                orientation = androidx.appcompat.widget.LinearLayoutCompat.HORIZONTAL
                setPadding(8, 6, 8, 6)
                gravity = if (viewType == 1) Gravity.START else Gravity.END
            }

            val tv = TextView(parent.context).apply {
                layoutParams = ViewGroup.MarginLayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply { setMargins(8, 6, 8, 6) }
                setTextColor(Color.WHITE)
                textSize = 15f
                setPadding(24, 16, 24, 16)
                background = bubble(
                    color = if (viewType == 1)
                        Color.parseColor("#3282B8")   // IA (esquerda)
                    else
                        Color.parseColor("#1B4F72")   // Usuário (direita)
                )
                maxWidth = (parent.resources.displayMetrics.widthPixels * 0.75).toInt()
            }

            container.addView(tv)
            return VH(container, tv)
        }

        override fun onBindViewHolder(holder: VH, position: Int) {
            holder.bind(items[position])
        }

        class VH(itemView: ViewGroup, private val tv: TextView) :
            RecyclerView.ViewHolder(itemView) {
            fun bind(m: ChatMsg) { tv.text = m.text }
        }

        private fun bubble(color: Int): GradientDrawable =
            GradientDrawable().apply {
                shape = GradientDrawable.RECTANGLE
                cornerRadii = floatArrayOf(28f,28f, 28f,28f, 28f,28f, 4f,4f)
                setColor(color)
            }
    }
}
