package com.example.projeto_programaomobile_parte1.data.model

enum class Unidade(val titulo: String) {
    UN("Un"),
    KG("Kg"),
    G("g"),
    L("L"),
    ML("mL"),
    CX("Cx"),
    PCT("Pct");

    override fun toString(): String = titulo
}

