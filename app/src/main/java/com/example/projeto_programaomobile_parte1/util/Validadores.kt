package com.example.projeto_programaomobile_parte1.util

object Validadores {
    fun naoVazio(texto: String?): Boolean = !texto.isNullOrBlank()
}

