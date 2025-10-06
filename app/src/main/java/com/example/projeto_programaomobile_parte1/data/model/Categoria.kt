package com.example.projeto_programaomobile_parte1.data.model

enum class Categoria(val titulo: String) {
    ALIMENTOS("Alimentos"),
    BEBIDAS("Bebidas"),
    HIGIENE("Higiene"),
    LIMPEZA("Limpeza"),
    OUTROS("Outros");

    override fun toString(): String = titulo
}

