# 📚 Biblifor

**Biblifor** é um aplicativo Android desenvolvido em **Kotlin** que simula um **sistema digital de biblioteca**, permitindo a gestão de acervo, empréstimos, renovações, favoritos e comunicação entre usuários e administradores.  
O projeto foi desenvolvido **em equipe**, no contexto acadêmico, e posteriormente organizado neste repositório para fins **educacionais e de portfólio**.

---

## 🎯 Objetivo do Projeto

O objetivo do Biblifor é oferecer uma solução mobile para bibliotecas acadêmicas, centralizando em um único aplicativo:

- Consulta ao acervo
- Empréstimos e renovações
- Organização de favoritos
- Avisos e mensagens
- Separação clara de papéis entre **usuário** e **administrador**

---

## 🛠️ Tecnologias Utilizadas

- **Kotlin**
- **Android SDK**
- **Firebase Firestore**
- **Firebase Authentication**
- **Android Studio**
- **Gradle (KTS)**
- **RecyclerView**
- **Git & GitHub**

> ⚠️ O projeto utilizou o **período de testes do Firebase**.  
> O arquivo `google-services.json` **não é versionado**, conforme boas práticas de segurança.

---

## 👥 Perfis de Usuário

### 👤 Usuário (Aluno)
- Login e cadastro
- Visualização do acervo
- Empréstimo de livros
- Renovação de empréstimos
- Favoritar livros
- Acesso a recomendações
- Visualização de avisos
- Histórico de empréstimos

### 🛡️ Administrador
- Cadastro e edição de livros
- Controle de empréstimos
- Gerenciamento de cápsulas/status
- Envio de avisos e mensagens
- Visualização de resultados e estatísticas

---

## 🧩 Principais Funcionalidades

- 📖 Acervo digital pesquisável  
- 🔄 Renovação de empréstimos  
- ⭐ Sistema de favoritos  
- 🔔 Avisos para usuários  
- 💬 Mensagens no sistema  
- 📊 Histórico de empréstimos  
- 🔐 Autenticação com Firebase  
- 🧭 Menus separados para usuário e administrador  

---

## 🗂️ Estrutura do Projeto

```text
biblifor/
├── app/
│   ├── src/main/java/com/example/biblifor/
│   │   ├── activities
│   │   ├── adapters
│   │   ├── models
│   │   └── util
│   ├── res/
│   │   ├── layout/
│   │   ├── drawable/
│   │   └── values/
├── docs/
│   ├── Documento_Requisitos_Modelagem_Sistemas_.pdf
│   └── BIBLIFOR_Narak.pdf
├── AUTHORS.md
├── README.md
└── .gitignore

---

## 🔧 Como Executar o Projeto

### Pré-requisitos

- Android Studio
- Emulador Android (recomendado: Pixel 6a – API 33)
- Conta Firebase (opcional, para testes completos)

---

### 🚀 Passos para Execução

1. Clone o repositório:
   git clone https://github.com/sergiomoita/biblifor-app.git

2. Abra o projeto no Android Studio

3. Configure seu próprio Firebase  
   Adicione o arquivo:
   app/google-services.json

4. Execute o aplicativo no emulador ou em um dispositivo físico

---

## 📄 Documentação

A documentação do projeto está disponível na pasta:

/docs

Inclui:

- Requisitos funcionais e não funcionais
- Modelagem do sistema
- Descrição dos principais fluxos da aplicação

---

## 👨‍💻 Autoria e Créditos

Este projeto foi desenvolvido em equipe como trabalho acadêmico.

A organização do repositório, manutenção do código e documentação para fins de portfólio foram realizadas por:

Sérgio Moita  
GitHub: https://github.com/sergiomoita

Os demais colaboradores estão listados no arquivo AUTHORS.md.

---

## ⚠️ Observações Importantes

- Projeto de caráter educacional
- Não destinado a uso em produção
- Firebase utilizado exclusivamente para testes
- Arquitetura baseada em Activities

---

## 📌 Status do Projeto

✔️ Concluído  
📦 Versão estável para fins acadêmicos e de portfólio
