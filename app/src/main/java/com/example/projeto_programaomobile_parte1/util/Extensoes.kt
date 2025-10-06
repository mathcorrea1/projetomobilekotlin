package com.example.projeto_programaomobile_parte1.util

import android.view.View
import com.google.android.material.textfield.TextInputLayout

fun View.mostrar(visible: Boolean) { this.visibility = if (visible) View.VISIBLE else View.GONE }

fun TextInputLayout.texto(): String = this.editText?.text?.toString()?.trim() ?: ""

