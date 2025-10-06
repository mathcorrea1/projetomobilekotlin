package com.example.projeto_programaomobile_parte1.domain

fun validarEmail(email: String): Boolean {
    val regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
    return email.matches(regex)
}

