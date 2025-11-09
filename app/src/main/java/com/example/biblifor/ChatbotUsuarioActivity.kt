package com.example.biblifor

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.launch

class ChatbotUsuarioActivity : BaseActivity() {

    // === Gemini ===
    private lateinit var generativeModel: GenerativeModel
    private var prePrompt: String = "prePrompt = \"\"\"\n" +
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
            "- Responda em 1–3 parágrafos curtos.\n" +
            "- Use listas apenas quando pedir “passo a passo”.\n" +
            "- Não inclua código, links externos ou informações não confirmadas.\n" +
            "\n" +
            "REGRAS DE CORRESPONDÊNCIA DE FAQ (MUITO IMPORTANTE):\n" +
            "- Se a pergunta do usuário corresponder ao tema/ intenção de alguma FAQ abaixo (mesmo por sinônimos ou variações simples), RETORNE EXATAMENTE a resposta canônica indicada — SEM reescrever, resumir ou acrescentar comentários. \n" +
            "- Quando usar uma FAQ, devolva somente o texto da “RESPOSTA” daquela FAQ (sem título e sem observações extras).\n" +
            "- Se a pergunta não corresponder às FAQs, responda normalmente seguindo o TOM e a PERSONA acima.\n" +
            "\n" +
            "================= FAQ CANÔNICAS (TÍTULO -> RESPOSTA) =================\n" +
            "\n" +
            "[Introdução/Contexto “Perguntas Frequentes”]\n" +
            "-> Tudo é novo e empolgante quando você começa a explorar a nossa biblioteca. Queremos que você se sinta bem-vindo e aproveite ao máximo todos os serviços. Por isso, respondemos aqui as principais dúvidas de quem está começando a utilizar o aplicativo ou visitar a Biblioteca.\n" +
            "\n" +
            "[Cadastro e acesso | gatilhos: cadastro, criar acesso, como acessar, login, senha, esqueci a senha, recuperar senha, RA, matrícula]\n" +
            "-> Para usar os serviços da Biblioteca Unifor, é preciso estar vinculado à universidade como aluno, professor ou colaborador. O acesso é gerado automaticamente pelas informações institucionais e pode ser feito com o número de matrícula (RA) e a senha padrão. Em caso de esquecimento ou dificuldade, a senha pode ser recuperada no aplicativo ou com o suporte da biblioteca.\n" +
            "\n" +
            "[Empréstimo e devoluções | gatilhos: empréstimo, pegar livro, devolução, renovar, prazo de empréstimo, cartão institucional]\n" +
            "-> O empréstimo de livros da Biblioteca Unifor é exclusivo para alunos, professores e colaboradores. Para retirar, apresente o cartão institucional. O prazo é de 7 dias, com renovação online se não houver reserva. O acesso é feito com a sua matrícula (RA) e senha. Em caso de dívidas, o serviço da biblioteca fica bloqueado.\n" +
            "\n" +
            "[Multas e prazos | gatilhos: multa, valor da multa, atraso, prazos, renovação com atraso]\n" +
            "-> O atraso na devolução de livros gera multa de R\$ 2,50 por dia e exemplar. O prazo é de 7 dias para alunos e 14 para professores. A renovação só é permitida se não houver reserva. As multas devem ser quitadas no balcão antes de novos empréstimos.\n" +
            "\n" +
            "[Pesquisa de acervo | gatilhos: como pesquisar, acervo, reserva online, disponibilidade, procurar livro, autor, título, assunto]\n" +
            "-> A pesquisa do acervo pode ser feita online com matrícula e senha. É possível fazer reservas de livros por título, autor ou assunto, além de verificar reservas e disponibilidade. Para mais orientações, o suporte está disponível no balcão da biblioteca.\n" +
            "\n" +
            "[Serviços extras | gatilhos: ABNT, Lattes, apoio acadêmico, serviços extras, orientação acadêmica]\n" +
            "-> A biblioteca oferece serviços de apoio acadêmico, como análise de normas da ABNT, auxílio no currículo Lattes e empréstimo de livros. Os usuários contam com orientações presenciais e online, garantindo suporte à produção e à pesquisa científica.\n" +
            "\n" +
            "================= FIM DAS FAQ =================\n" +
            "\n" +
            "QUANDO NÃO FOR FAQ:\n" +
            "- Responda de modo objetivo, com foco em como fazer no app (ex.: onde clicar, onde ver prazo).\n" +
            "- Se a dúvida exigir informação dinâmica (ex.: status de reserva em tempo real), diga que esses dados aparecem no próprio app (Minha Conta/Meus Empréstimos/Reservas) e oriente o caminho.\n" +
            "- Em casos fora do escopo, diga com gentileza que não possui essa informação e sugira procurar o balcão da biblioteca ou o suporte no aplicativo.\n" +
            "\n" +
            "CONFIRMAÇÃO FINAL:\n" +
            "- Ao concluir, pergunte educadamente se a resposta ajudou ou se deseja mais alguma orientação.\n" +
            "\"\"\"\n" // moldando o comportamento da IA

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_chatbot_usuario)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // ---- Navegação existente (mantido) ----
        val leoSetaVoltarChatbot7 = findViewById<ImageView>(R.id.leoImagemSetaVoltarChatbot7)
        leoSetaVoltarChatbot7.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        val leoLogoHomeBFChatbot7 = findViewById<ImageView>(R.id.leoLogoHomeChatbotBF7)
        leoLogoHomeBFChatbot7.setOnClickListener {
            val navegarHomeChat7 = Intent(this, MenuPrincipalUsuarioActivity::class.java)
            startActivity(navegarHomeChat7)
        }
        val leoLogoChatbotBFChatbot7 = findViewById<ImageView>(R.id.leoImagemChatbotBF7)
        leoLogoChatbotBFChatbot7.setOnClickListener {
            val navegarChatbotChat7 = Intent(this, ChatbotUsuarioActivity::class.java)
            startActivity(navegarChatbotChat7)
        }
        val leoLogoNotificacoesBFChatbot7 = findViewById<ImageView>(R.id.leoImagemNotificacoesChatbotBF7)
        leoLogoNotificacoesBFChatbot7.setOnClickListener {
            val navegarNotificacoesChat7 = Intent(this, AvisosUsuarioActivity::class.java)
            startActivity(navegarNotificacoesChat7)
        }
        val leoLogoMenuBFChatbot7 = findViewById<ImageView>(R.id.leoImagemMenuChatbotBF7)
        leoLogoMenuBFChatbot7.setOnClickListener {
            val navegarMenuChat7 = Intent(this, MenuPrincipalUsuarioActivity::class.java)
            startActivity(navegarMenuChat7)
        }

        // ---- Views usadas pela IA (reaproveitando seu layout atual) ----
        val etPrompt = findViewById<EditText>(R.id.leoCampoEnviarMensagemChatbot7)
        val btnSend = findViewById<ImageView>(R.id.leoBotaoEnviarMensagemChatbot7)
        val tvUserLast = findViewById<TextView>(R.id.leoMensagemUsuario2Chatbot7)
        val tvAiAnswer = findViewById<TextView>(R.id.leoMensagemMoema2Chatbot7)

        // ---- Config Gemini (modelo do professor) ----
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

            // mostra mensagem do usuário e um “carregando…” no balão da IA
            tvUserLast.text = userText
            tvAiAnswer.text = "Pensando…"

            btnSend.isEnabled = false

            lifecycleScope.launch {
                try {
                    val response = generativeModel.generateContent(prePrompt + userText)
                    val txt = response.text ?: "Sem resposta do modelo"
                    tvAiAnswer.text = txt
                } catch (e: Exception) {
                    tvAiAnswer.text = "Erro ao gerar resposta."
                    Toast.makeText(
                        this@ChatbotUsuarioActivity,
                        "Falha: ${e.localizedMessage}",
                        Toast.LENGTH_SHORT
                    ).show()
                } finally {
                    btnSend.isEnabled = true
                }
            }
        }
    }
}
