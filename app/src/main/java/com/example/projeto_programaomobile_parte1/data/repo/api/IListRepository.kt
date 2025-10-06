package com.example.projeto_programaomobile_parte1.data.repo.api

import android.net.Uri
import com.example.projeto_programaomobile_parte1.data.model.Categoria
import com.example.projeto_programaomobile_parte1.data.model.Item
import com.example.projeto_programaomobile_parte1.data.model.ListaCompra
import com.example.projeto_programaomobile_parte1.data.model.Unidade
import com.example.projeto_programaomobile_parte1.util.Resultado

interface IListRepository {
    fun obterListas(): List<ListaCompra>
    fun adicionarLista(titulo: String, imagemUri: Uri?): Resultado<ListaCompra>
    fun editarLista(id: String, novoTitulo: String, novaImagem: Uri?): Resultado<ListaCompra>
    fun excluirLista(id: String): Resultado<Unit>

    fun obterItens(listaId: String): List<Item>
    fun adicionarItem(
        listaId: String,
        nome: String,
        quantidade: Double,
        unidade: Unidade,
        categoria: Categoria
    ): Resultado<Item>
    fun editarItem(item: Item): Resultado<Item>
    fun removerItem(itemId: String): Resultado<Unit>
    fun marcarComoComprado(itemId: String): Resultado<Unit>
    fun desmarcarComoComprado(itemId: String): Resultado<Unit>
    fun limpar()
}
