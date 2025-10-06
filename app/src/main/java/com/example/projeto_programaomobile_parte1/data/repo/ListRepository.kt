package com.example.projeto_programaomobile_parte1.data.repo

import android.net.Uri
import com.example.projeto_programaomobile_parte1.data.model.Categoria
import com.example.projeto_programaomobile_parte1.data.model.Item
import com.example.projeto_programaomobile_parte1.data.model.ListaCompra
import com.example.projeto_programaomobile_parte1.data.model.Unidade
import com.example.projeto_programaomobile_parte1.data.repo.api.IListRepository
import com.example.projeto_programaomobile_parte1.util.Resultado
import java.util.UUID

object ListRepository : IListRepository {
    private val listas = mutableListOf<ListaCompra>()
    private val itensPorLista = mutableMapOf<String, MutableList<Item>>()

    override fun obterListas(): List<ListaCompra> = listas.toList()

    override fun adicionarLista(titulo: String, imagemUri: Uri?): Resultado<ListaCompra> {
        val lista = ListaCompra(UUID.randomUUID().toString(), titulo, imagemUri)
        listas += lista
        itensPorLista[lista.id] = mutableListOf()
        return Resultado.Sucesso(lista)
    }

    override fun editarLista(id: String, novoTitulo: String, novaImagem: Uri?): Resultado<ListaCompra> {
        val idx = listas.indexOfFirst { it.id == id }
        if (idx == -1) return Resultado.Erro("Lista não encontrada")

        val listaAtualizada = ListaCompra(id, novoTitulo, novaImagem)
        listas[idx] = listaAtualizada
        return Resultado.Sucesso(listaAtualizada)
    }

    override fun excluirLista(id: String): Resultado<Unit> {
        val removida = listas.removeIf { it.id == id }
        itensPorLista.remove(id)
        return if (removida) Resultado.Sucesso(Unit) else Resultado.Erro("Lista não encontrada")
    }

    override fun obterItens(listaId: String): List<Item> = itensPorLista[listaId]?.toList() ?: emptyList()

    override fun adicionarItem(listaId: String, nome: String, quantidade: Double, unidade: Unidade, categoria: Categoria): Resultado<Item> {
        val item = Item(listaId = listaId, nome = nome, quantidade = quantidade, unidade = unidade, categoria = categoria)
        val lista = itensPorLista[listaId] ?: return Resultado.Erro("Lista inválida")
        lista += item
        return Resultado.Sucesso(item)
    }

    override fun editarItem(item: Item): Resultado<Item> {
        val lista = itensPorLista[item.listaId] ?: return Resultado.Erro("Lista inválida")
        val idx = lista.indexOfFirst { it.id == item.id }
        if (idx == -1) return Resultado.Erro("Item não encontrado")
        lista[idx] = item
        return Resultado.Sucesso(item)
    }

    override fun removerItem(itemId: String): Resultado<Unit> {
        itensPorLista.forEach { (_, lista) ->
            val removed = lista.removeIf { it.id == itemId }
            if (removed) return Resultado.Sucesso(Unit)
        }
        return Resultado.Erro("Item não encontrado")
    }

    override fun marcarComoComprado(itemId: String): Resultado<Unit> {
        val item = encontrar(itemId) ?: return Resultado.Erro("Item não encontrado")
        item.comprado = true
        return Resultado.Sucesso(Unit)
    }

    override fun desmarcarComoComprado(itemId: String): Resultado<Unit> {
        val item = encontrar(itemId) ?: return Resultado.Erro("Item não encontrado")
        item.comprado = false
        return Resultado.Sucesso(Unit)
    }

    private fun encontrar(id: String): Item? = itensPorLista.values.flatten().find { it.id == id }

    override fun limpar() {
        listas.clear()
        itensPorLista.clear()
    }
}
