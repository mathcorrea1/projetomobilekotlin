package com.example.projeto_programaomobile_parte1.data.model

import java.util.UUID

data class Item(
    val id: String = UUID.randomUUID().toString(),
    val listaId: String,
    var nome: String,
    var quantidade: Double,
    var unidade: Unidade,
    var categoria: Categoria,
    var comprado: Boolean = false
)

