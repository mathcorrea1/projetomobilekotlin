package com.example.projeto_programaomobile_parte1.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projeto_programaomobile_parte1.di.ServiceLocator
import com.example.projeto_programaomobile_parte1.domain.validarEmail
import com.example.projeto_programaomobile_parte1.util.Resultado

class LoginViewModel : ViewModel() {
    private val auth = ServiceLocator.authRepository()

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _erro = MutableLiveData<String?>()
    val erro: LiveData<String?> = _erro

    private val _sucesso = MutableLiveData<Boolean>()
    val sucesso: LiveData<Boolean> = _sucesso

    fun efetuarLogin(email: String, senha: String) {
        _erro.value = null
        if (!validarEmail(email)) { _erro.value = "E-mail inválido"; return }
        if (senha.isBlank()) { _erro.value = "Senha obrigatória"; return }
        _loading.value = true
        val res = auth.efetuarLogin(email, senha)
        _loading.value = false
        when (res) {
            is Resultado.Sucesso -> _sucesso.value = true
            is Resultado.Erro -> _erro.value = res.mensagem
        }
    }
}

