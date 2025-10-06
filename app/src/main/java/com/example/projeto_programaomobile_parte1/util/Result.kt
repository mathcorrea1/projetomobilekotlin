package com.example.projeto_programaomobile_parte1.util

sealed class Resultado<out T> {
    data class Sucesso<out T>(val dado: T) : Resultado<T>()
    data class Erro(val mensagem: String) : Resultado<Nothing>()
}

