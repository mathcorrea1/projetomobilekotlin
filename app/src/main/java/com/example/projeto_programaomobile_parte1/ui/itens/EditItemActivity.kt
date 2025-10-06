package com.example.projeto_programaomobile_parte1.ui.itens

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.example.projeto_programaomobile_parte1.R
import com.example.projeto_programaomobile_parte1.data.model.Categoria
import com.example.projeto_programaomobile_parte1.data.model.Unidade
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout

class EditItemActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_ID = "extra_id"
        const val EXTRA_LISTA_ID = "extra_lista_id"
        const val EXTRA_NOME = "extra_nome"
        const val EXTRA_QTD = "extra_qtd"
        const val EXTRA_UNIDADE = "extra_unidade"
        const val EXTRA_CATEGORIA = "extra_categoria"
        const val EXTRA_COMPRADO = "extra_comprado"
        const val EXTRA_DELETE = "extra_delete"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_item)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        val tilNome = findViewById<TextInputLayout>(R.id.tilNome)
        val tilQuantidade = findViewById<TextInputLayout>(R.id.tilQuantidade)
        val spnUnidade = findViewById<Spinner>(R.id.spnUnidade)
        val spnCategoria = findViewById<Spinner>(R.id.spnCategoria)
        val btnSalvar = findViewById<MaterialButton>(R.id.btnSalvar)
        val btnExcluir = findViewById<MaterialButton>(R.id.btnExcluir)

        toolbar.setNavigationOnClickListener { finish() }

        val unidades = Unidade.entries
        val categorias = Categoria.entries
        spnUnidade.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, unidades)
        spnCategoria.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, categorias)

        val id = intent.getStringExtra(EXTRA_ID).orEmpty()
        val listaId = intent.getStringExtra(EXTRA_LISTA_ID).orEmpty()
        val nome = intent.getStringExtra(EXTRA_NOME).orEmpty()
        val qtd = intent.getDoubleExtra(EXTRA_QTD, 0.0)
        val unidadeName = intent.getStringExtra(EXTRA_UNIDADE)
        val categoriaName = intent.getStringExtra(EXTRA_CATEGORIA)
        val comprado = intent.getBooleanExtra(EXTRA_COMPRADO, false)

        tilNome.editText?.setText(nome)
        tilQuantidade.editText?.setText(if (qtd == 0.0) "" else qtd.toString())
        unidadeName?.let { name ->
            val idx = unidades.indexOfFirst { it.name == name }
            if (idx >= 0) spnUnidade.setSelection(idx)
        }
        categoriaName?.let { name ->
            val idx = categorias.indexOfFirst { it.name == name }
            if (idx >= 0) spnCategoria.setSelection(idx)
        }

        btnSalvar.setOnClickListener {
            val novoNome = tilNome.editText?.text?.toString()?.trim().orEmpty()
            val qtdStr = tilQuantidade.editText?.text?.toString()?.replace(",", ".")
            val novaQtd = qtdStr?.toDoubleOrNull() ?: 0.0

            var valido = true
            if (novoNome.isBlank()) { tilNome.error = getString(R.string.erro_campo_obrigatorio); valido = false } else { tilNome.error = null }
            if (novaQtd <= 0) { tilQuantidade.error = getString(R.string.erro_campo_obrigatorio); valido = false } else { tilQuantidade.error = null }
            if (!valido) return@setOnClickListener

            val unidade = unidades[spnUnidade.selectedItemPosition]
            val categoria = categorias[spnCategoria.selectedItemPosition]

            val data = Intent().apply {
                putExtra(EXTRA_ID, id)
                putExtra(EXTRA_LISTA_ID, listaId)
                putExtra(EXTRA_NOME, novoNome)
                putExtra(EXTRA_QTD, novaQtd)
                putExtra(EXTRA_UNIDADE, unidade.name)
                putExtra(EXTRA_CATEGORIA, categoria.name)
                putExtra(EXTRA_COMPRADO, comprado)
            }
            setResult(RESULT_OK, data)
            finish()
        }

        btnExcluir.setOnClickListener {
            val data = Intent().apply {
                putExtra(EXTRA_ID, id)
                putExtra(EXTRA_DELETE, true)
            }
            setResult(RESULT_OK, data)
            finish()
        }
    }
}

