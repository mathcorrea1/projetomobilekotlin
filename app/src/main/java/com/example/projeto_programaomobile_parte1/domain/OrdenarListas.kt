package com.example.projeto_programaomobile_parte1.domain

import com.example.projeto_programaomobile_parte1.data.model.ListaCompra

fun ordenarListas(listas: List<ListaCompra>): List<ListaCompra> = listas.sortedBy { it.titulo.lowercase() }

