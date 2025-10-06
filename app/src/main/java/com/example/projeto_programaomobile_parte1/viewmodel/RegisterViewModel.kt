package com.example.projeto_programaomobile_parte1.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projeto_programaomobile_parte1.di.ServiceLocator
import com.example.projeto_programaomobile_parte1.domain.validarEmail
import com.example.projeto_programaomobile_parte1.util.Resultado

class RegisterViewModel : ViewModel() {
    private val auth = ServiceLocator.authRepository()

    private val _mensagem = MutableLiveData<String?>()
    val mensagem: LiveData<String?> = _mensagem

    private val _erro = MutableLiveData<String?>()
    val erro: LiveData<String?> = _erro

    fun cadastrar(nome: String, email: String, senha: String, confirmar: String) {
        _erro.value = null
        if (nome.isBlank()) { _erro.value = "Nome obrigatório"; return }
        if (!validarEmail(email)) { _erro.value = "E-mail inválido"; return }
        if (senha.isBlank() || confirmar.isBlank()) { _erro.value = "Senha obrigatória"; return }
        if (senha != confirmar) { _erro.value = "As senhas não conferem"; return }
        when (val res = auth.cadastrar(nome, email, senha)) {
            is Resultado.Sucesso -> _mensagem.value = "Conta criada! Faça login."
            is Resultado.Erro -> _erro.value = res.mensagem
        }
    }
}

