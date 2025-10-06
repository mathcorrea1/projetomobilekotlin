package com.example.projeto_programaomobile_parte1.data.repo.api

import com.example.projeto_programaomobile_parte1.data.model.Usuario
import com.example.projeto_programaomobile_parte1.util.Resultado

interface IAuthRepository {
    fun cadastrar(nome: String, email: String, senha: String): Resultado<Usuario>
    fun efetuarLogin(email: String, senha: String): Resultado<Usuario>
    fun efetuarLogout()
    fun usuarioAtual(): Usuario?
}

