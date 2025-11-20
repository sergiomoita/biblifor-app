package com.example.biblifor

data class Book(
    val title: String,
    val coverRes: Int,              // fallback de capa (caso não tenha imagem)
    val emprestavel: Boolean,       // true se "Emprestável"
    val imagemBase64: String? = null
)
