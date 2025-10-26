package com.example.biblifor

data class HistoryBook(
    val title: String,
    val author: String,
    val coverRes: Int,
    val availabilityText: String,
    val isAvailable: Boolean,
    val dateText: String,           // ex: "13/09/2025"
    val statusText: String? = null  // ex: "Empréstimo em finalizado" (opcional)
)
