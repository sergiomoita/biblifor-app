package com.example.biblifor

data class Book(
    val title: String,
    val coverRes: Int,
    val emprestavel: Boolean,
    val imagemBase64: String? = null,

    // ===== Novos atributos (opcionais, usados só no popup) =====
    val tituloOriginal: String = "",
    val autor: String = "",
    val situacaoEmprestimo: String = "",
    val disponibilidade: String = "",

    // ===== ID DO DOCUMENTO NO FIRESTORE =====
    val livroId: String = "",

    // ===== DATA PARA ORDENAR NO HISTÓRICO =====
    val dataDevolucao: String = ""   // <-- ADICIONADO AQUI
)
