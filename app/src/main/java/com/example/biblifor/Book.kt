data class Book(
    val title: String,
    val coverRes: Int,
    val emprestavel: Boolean,
    val imagemBase64: String? = null,

    // ===== Novos atributos (opcionais, usados só no popup) =====
    val tituloOriginal: String = "",
    val autor: String = "",
    val situacaoEmprestimo: String = "",
    val disponibilidade: String = ""
)
