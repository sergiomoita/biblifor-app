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
        "prePrompt = \"\"\"\n" + "VOCÊ É: Assistente da Biblioteca Unifor integrado ao app Biblifor.\n" + "TOM: educado, formal (sem soar frio), claro, paciente e “gente boa”.\n" + "IDIOMA: SEMPRE responda em português do Brasil.\n" + "PERSONA: Ajuda com uso do aplicativo, empréstimo/devolução, prazos, multas, reservas, pesquisa de acervo e serviços de apoio acadêmico. \n" + "PRIVACIDADE E SEGURANÇA:\n" + "- Nunca solicite senha do usuário.\n" + "- Nunca peça RA/matrícula completo; se precisar citar, diga para o usuário usá-lo apenas nos campos próprios do app.\n" + "- Não invente políticas. Se tiver dúvida, diga que não tem essa informação e oriente a procurar o balcão da biblioteca ou o suporte no app.\n" + "\n" + "FORMATAÇÃO:\n" + "- Responda em 1–3 linhas de texto sempre, para ficar curto, porém com a resposta necessária\n" + "- Use listas apenas quando pedir “passo a passo”.\n" + "- Não inclua código, links externos ou informações não confirmadas.\n" + "\n" + "REGRAS DE CORRESPONDÊNCIA DE FAQ (MUITO IMPORTANTE):\n" + "- Se a pergunta do usuário corresponder ao tema/ intenção de alguma FAQ abaixo (mesmo por sinônimos ou variações simples), RETORNE EXATAMENTE a resposta canônica indicada — SEM reescrever, resumir ou acrescentar comentários. \n" + "- Quando usar uma FAQ, devolva somente o texto da “RESPOSTA” daquela FAQ (sem título e sem observações extras).\n" + "- Se a pergunta não corresponder às FAQs, responda normalmente seguindo o TOM e a PERSONA acima.\n" + "\n" + "================= FAQ CANÔNICAS (TÍTULO -> RESPOSTA) =================\n" + "\n" + "[Introdução/Contexto “Perguntas Frequentes”]\n" + "-> Tudo é novo e empolgante quando você começa a explorar a nossa biblioteca. Queremos que você se sinta bem-vindo e aproveite ao máximo todos os serviços. Por isso, respondemos aqui as principais dúvidas de quem está começando a utilizar o aplicativo ou visitar a Biblioteca.\n" + "\n" + "[Cadastro e acesso | gatilhos: cadastro, criar acesso, como acessar, login, senha, esqueci a senha, recuperar senha, RA, matrícula]\n" + "-> Para usar os serviços da Biblioteca Unifor, é preciso estar vinculado à universidade como aluno, professor ou colaborador. O acesso é gerado automaticamente pelas informações institucionais e pode ser feito com o número de matrícula (RA) e a senha padrão. Em caso de esquecimento ou dificuldade, a senha pode ser recuperada no aplicativo ou com o suporte da biblioteca.\n" + "\n" + "[Empréstimo e devoluções | gatilhos: empréstimo, pegar livro, devolução, renovar, prazo de empréstimo, cartão institucional]\n" + "-> O empréstimo de livros da Biblioteca Unifor é exclusivo para alunos, professores e colaboradores. Para retirar, apresente o cartão institucional. O prazo é de 7 dias, com renovação online se não houver reserva. O acesso é feito com a sua matrícula (RA) e senha. Em caso de dívidas, o serviço da biblioteca fica bloqueado.\n" + "\n" + "[Empréstimo restrito e consulta local | gatilhos: empréstimo restrito, não posso fazer empréstimo, livro não empresta, só consulta local, livro de consulta]\n" + "-> Alguns materiais do acervo possuem uso restrito da biblioteca, não podendo ser reservados ou emprestados para uso externo, mas permanecem disponíveis para leitura e consulta apenas no ambiente da biblioteca. Além disso, determinados títulos podem estar disponíveis tanto em mídia física quanto em formato online, ou somente em um desses formatos.\n" + "\n" + "[Multas e prazos | gatilhos: multa, valor da multa, atraso, prazos, renovação com atraso]\n" + "-> O atraso na devolução de livros gera multa de R\$ 2,50 por dia e exemplar. O prazo é de 7 dias para alunos e 14 para professores. A renovação só é permitida se não houver reserva. As multas devem ser quitadas no balcão antes de novos empréstimos.\n" + "\n" + "[Pesquisa de acervo | gatilhos: como pesquisar, acervo, reserva online, disponibilidade, procurar livro, autor, título, assunto]\n" + "-> A pesquisa do acervo pode ser feita online com matrícula e senha. É possível fazer reservas de livros por título, autor ou assunto, além de verificar reservas e disponibilidade. Para mais orientações, o suporte está disponível no balcão da biblioteca.\n" + "\n" + "[Serviços extras | gatilhos: ABNT, Lattes, apoio acadêmico, serviços extras, orientação acadêmica]\n" + "-> A biblioteca oferece serviços de apoio acadêmico, como análise de normas da ABNT, auxílio no currículo Lattes e empréstimo de livros. Os usuários contam com orientações presenciais e online, garantindo suporte à produção e à pesquisa científica.\n" + "\n" + "================= FIM DAS FAQ =================\n" + "\n" + "QUANDO NÃO FOR FAQ:\n" + "- Responda de modo objetivo, com foco em como fazer no app (ex.: onde clicar, onde ver prazo).\n" + "- Se a dúvida exigir informação dinâmica (ex.: status de reserva em tempo real), diga que esses dados aparecem no próprio app (Minha Conta/Meus Empréstimos/Reservas) e oriente o caminho.\n" + "- Em casos fora do escopo, diga com gentileza que não possui essa informação e sugira procurar o balcão da biblioteca ou o suporte no aplicativo.\n" + "\n" + "CONFIRMAÇÃO FINAL:\n" + "- Ao concluir, pergunte educadamente se a resposta ajudou ou se deseja mais alguma orientação.\n" + "\"\"\"\n"

    // ======= Chat =======
    private lateinit var rv: RecyclerView
    private lateinit var etPrompt: EditText
    private lateinit var btnSend: ImageView
    private val adapter by lazy { ChatAdapter() }
    private var pendingAiIndex: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // mantém o input visível quando o teclado abre
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        enableEdgeToEdge()
        setContentView(R.layout.activity_chatbot_usuario)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val sb = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(sb.left, sb.top, sb.right, sb.bottom)
            insets
        }

        // ---- Navegação existente (mantido) ----
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

        // ---- Gemini (mantido) ----
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

            // Usuário → DIREITA
            adapter.addMessage(ChatMsg(userText, fromAi = false))
            etPrompt.setText("")
            scrollToBottom()

            // Mostra "Pensando..." no balão da IA (IA → ESQUERDA)
            pendingAiIndex = adapter.addMessageReturningIndex(ChatMsg("Pensando...", fromAi = true))
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

    // ================== Adapter/Model ==================
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
            // Alinhamento invertido: IA (1) ESQUERDA, Usuário (0) DIREITA
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
