package com.example.projeto_programaomobile_parte1.data.repo

import com.example.projeto_programaomobile_parte1.data.model.Usuario
import com.example.projeto_programaomobile_parte1.data.repo.api.IAuthRepository
import com.example.projeto_programaomobile_parte1.util.Resultado
import java.util.UUID

object AuthRepository : IAuthRepository {
    private val usuarios = mutableListOf<Usuario>()
    private var logado: Usuario? = null

    override fun cadastrar(nome: String, email: String, senha: String): Resultado<Usuario> {
        if (usuarios.any { it.email.equals(email, ignoreCase = true) }) {
            return Resultado.Erro("E-mail já cadastrado")
        }
        val u = Usuario(UUID.randomUUID().toString(), nome, email, senha)
        usuarios += u
        return Resultado.Sucesso(u)
    }

    override fun efetuarLogin(email: String, senha: String): Resultado<Usuario> {
        val u = usuarios.find { it.email.equals(email, true) && it.senha == senha }
        return if (u != null) {
            logado = u
            Resultado.Sucesso(u)
        } else Resultado.Erro("Credenciais inválidas")
    }

    override fun efetuarLogout() { logado = null }

    override fun usuarioAtual(): Usuario? = logado
}

