package com.example.biblifor

data class Emprestimo(
    val nome: String = "",
    val autor: String = "",
    val dataEmprestimo: String = "",
    val dataDevolucao: String = "",
    val status: String = "",
    val livroId: String = "",
    val localizacao: String = "",
    val idDocumento: String = "",
    val imagemBase64: String? = null  // <-- precisa existir
)


