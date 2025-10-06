package com.example.projeto_programaomobile_parte1.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.example.projeto_programaomobile_parte1.data.model.ListaCompra
import com.example.projeto_programaomobile_parte1.di.ServiceLocator
import com.example.projeto_programaomobile_parte1.domain.ordenarListas
import com.example.projeto_programaomobile_parte1.util.Resultado

class ListsViewModel : ViewModel() {
    private val repo = ServiceLocator.listRepository()

    private val _busca = MutableLiveData("")
    val busca: LiveData<String> = _busca

    private val _listas = MutableLiveData<List<ListaCompra>>(repo.obterListas())
    val listasFiltradas: LiveData<List<ListaCompra>> = _listas.map { listaAtual ->
        val termo = _busca.value?.lowercase()?.trim().orEmpty()
        val filtradas = if (termo.isBlank()) listaAtual else listaAtual.filter { it.titulo.lowercase().contains(termo) }
        ordenarListas(filtradas)
    }

    private val _mensagem = MutableLiveData<String?>()
    val mensagem: LiveData<String?> = _mensagem

    fun atualizarBusca(termo: String) { _busca.value = termo; _listas.value = repo.obterListas() }

    fun adicionarLista(titulo: String, uri: Uri?) {
        when (val res = repo.adicionarLista(titulo, uri)) {
            is Resultado.Sucesso -> _listas.value = repo.obterListas()
            is Resultado.Erro -> _mensagem.value = res.mensagem
        }
    }

    fun editarLista(id: String, novoTitulo: String, novaImagem: Uri?) {
        when (val res = repo.editarLista(id, novoTitulo, novaImagem)) {
            is Resultado.Sucesso -> _listas.value = repo.obterListas()
            is Resultado.Erro -> _mensagem.value = res.mensagem
        }
    }

    fun excluirLista(id: String) {
        when (val res = repo.excluirLista(id)) {
            is Resultado.Sucesso -> {
                _mensagem.value = "Lista removida"
                _listas.value = repo.obterListas()
            }
            is Resultado.Erro -> _mensagem.value = res.mensagem
        }
    }
}
