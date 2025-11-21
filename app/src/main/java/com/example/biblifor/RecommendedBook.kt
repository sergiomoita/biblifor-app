package com.example.biblifor

data class RecommendedBook(
    val title: String,
    val coverRes: Int,
    val availabilityText: String,
    val available: Boolean,

    // ===== NOVOS CAMPOS OPCIONAIS =====
    val livroId: String = "",                 // ID do documento no Firestore
    val imagemBase64: String? = null,         // capa real
    val autor: String = "",
    val situacao: String = "",
    val disponibilidade: String = ""
)
