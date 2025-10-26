package com.example.biblifor

data class RecommendedBook(
    val title: String,
    val coverRes: Int,
    val availabilityText: String, // "Disponível: físico + digital", "Indisponível", etc.
    val available: Boolean      // só para pintar a cor (verde/vermelho)
)
