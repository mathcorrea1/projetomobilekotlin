package com.example.projeto_programaomobile_parte1.domain

import com.example.projeto_programaomobile_parte1.data.model.Categoria
import com.example.projeto_programaomobile_parte1.data.model.Item

data class GrupoCategoria(val categoria: Categoria, val itens: List<Item>)

data class AgrupamentoItens(
    val aComprar: List<GrupoCategoria>,
    val comprados: List<GrupoCategoria>
)

fun agruparItensPorCategoria(itens: List<Item>): AgrupamentoItens {
    val aComprar = itens.filter { !it.comprado }
        .groupBy { it.categoria }
        .toSortedMap(compareBy { it.titulo })
        .map { (cat, lista) -> GrupoCategoria(cat, lista.sortedBy { it.nome.lowercase() }) }
    val comprados = itens.filter { it.comprado }
        .groupBy { it.categoria }
        .toSortedMap(compareBy { it.titulo })
        .map { (cat, lista) -> GrupoCategoria(cat, lista.sortedBy { it.nome.lowercase() }) }
    return AgrupamentoItens(aComprar, comprados)
}

