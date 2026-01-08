# Biblifor 📚
Aplicativo Android (Kotlin) para apoiar a experiência de alunos e administradores em um sistema de biblioteca: autenticação, pesquisa de acervo, empréstimos/renovação, favoritos, avisos e funcionalidades administrativas.

> Projeto acadêmico desenvolvido em equipe.  
> Este repositório é uma cópia pública para fins de portfólio, com créditos e documentação.

## ✨ Principais funcionalidades
### 👤 Aluno
- Login por **matrícula e senha**
- Menu principal e navegação por módulos
- Pesquisa de livros no acervo
- Favoritos e conteúdos recomendados
- Avisos e notificações
- Fluxo de empréstimo e confirmação
- Histórico de empréstimos (listagem/paginação)
- Acesso a conteúdo online quando disponível

### 🛠️ Administrador
- Cadastro e manutenção de livros (informações e disponibilidade)
- Gerenciamento de avisos/eventos
- Gerenciamento de “cápsulas”/salas e seus status (quando aplicável)

## 🧱 Tecnologias
- **Kotlin** + **Android Studio**
- Interface com **XML Layouts**
- (Período de teste) **Firebase** — configuração local
- Emulador recomendado: **Pixel 6a – API 33 (Android 13)**

## ▶️ Como executar o projeto
### Pré-requisitos
- Android Studio instalado
- Android SDK configurado (API 33 recomendado)
- Um emulador (AVD) ou celular físico com depuração USB

### Rodando
1. Clone este repositório
2. Abra a pasta do projeto no Android Studio
3. Aguarde o **Gradle Sync**
4. Inicie um emulador (ex.: Pixel 6a API 33)
5. Clique em **Run (▶)**

## 🔐 Firebase (observação importante)
Este repositório **não inclui** `google-services.json` por ser um arquivo de configuração local.
Para executar com Firebase:
1. Crie um projeto no Firebase (ou use um para testes)
2. Adicione um app Android com o mesmo `applicationId`
3. Baixe o `google-services.json` e coloque em:
   `app/google-services.json`
4. Faça Sync e rode novamente

## 📄 Documentação
- Requisitos e modelagem do projeto: ver a pasta [`docs/`](./docs)

## 🤝 Créditos
Projeto desenvolvido em equipe por:
- Sergio Moita
- Mateus Lopes Pinheiro
- Davi Ribeiro
- Leonardo Enzo Pinheiro Diógenes
- Gustavo Andrade Ferreira de Medeiros
