package com.example.biblifor.model

import com.google.firebase.Timestamp

data class Aviso(
    val titulo: String = "",
    val data: Timestamp? = null,
    val mensagem: String = "",
    val matricula: String = "",
    val matriculaAdm: String = ""
)
