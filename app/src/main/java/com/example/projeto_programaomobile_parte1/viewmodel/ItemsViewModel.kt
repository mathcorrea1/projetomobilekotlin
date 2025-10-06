package com.example.projeto_programaomobile_parte1.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.example.projeto_programaomobile_parte1.data.model.Categoria
import com.example.projeto_programaomobile_parte1.data.model.Item
import com.example.projeto_programaomobile_parte1.data.model.Unidade
import com.example.projeto_programaomobile_parte1.di.ServiceLocator
import com.example.projeto_programaomobile_parte1.domain.AgrupamentoItens
import com.example.projeto_programaomobile_parte1.domain.agruparItensPorCategoria
import com.example.projeto_programaomobile_parte1.util.Resultado

class ItemsViewModel(private val listaId: String) : ViewModel() {
    private val repo = ServiceLocator.listRepository()

    private val _busca = MutableLiveData("")
    val busca: LiveData<String> = _busca

    private val _itens = MutableLiveData<List<Item>>(repo.obterItens(listaId))

    val agrupados: LiveData<AgrupamentoItens> = _itens.map { itensAtuais ->
        val termo = _busca.value?.lowercase()?.trim().orEmpty()
        val filtrados = if (termo.isBlank()) itensAtuais else itensAtuais.filter { it.nome.lowercase().contains(termo) }
        agruparItensPorCategoria(filtrados)
    }

    private val _mensagem = MutableLiveData<String?>()
    val mensagem: LiveData<String?> = _mensagem

    fun atualizarBusca(termo: String) { _busca.value = termo; _itens.value = repo.obterItens(listaId) }

    fun adicionarItem(nome: String, quantidade: Double, unidade: Unidade, categoria: Categoria) {
        when (val res = repo.adicionarItem(listaId, nome, quantidade, unidade, categoria)) {
            is Resultado.Sucesso -> _itens.value = repo.obterItens(listaId)
            is Resultado.Erro -> _mensagem.value = res.mensagem
        }
    }

    fun editarItem(item: Item) {
        when (val res = repo.editarItem(item)) {
            is Resultado.Sucesso -> _itens.value = repo.obterItens(listaId)
            is Resultado.Erro -> _mensagem.value = res.mensagem
        }
    }

    fun removerItem(itemId: String) {
        when (val res = repo.removerItem(itemId)) {
            is Resultado.Sucesso -> {
                _mensagem.value = "Item removido"
                _itens.value = repo.obterItens(listaId)
            }
            is Resultado.Erro -> _mensagem.value = res.mensagem
        }
    }

    fun alternarComprado(item: Item) {
        val action = if (item.comprado) repo.desmarcarComoComprado(item.id) else repo.marcarComoComprado(item.id)
        when (action) {
            is Resultado.Sucesso -> _itens.value = repo.obterItens(listaId)
            is Resultado.Erro -> _mensagem.value = action.mensagem
        }
    }
}
