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

class AddItemActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_NOME = "extra_nome"
        const val EXTRA_QTD = "extra_qtd"
        const val EXTRA_UNIDADE = "extra_unidade"
        const val EXTRA_CATEGORIA = "extra_categoria"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        val tilNome = findViewById<TextInputLayout>(R.id.tilNome)
        val tilQuantidade = findViewById<TextInputLayout>(R.id.tilQuantidade)
        val spnUnidade = findViewById<Spinner>(R.id.spnUnidade)
        val spnCategoria = findViewById<Spinner>(R.id.spnCategoria)
        val btnSalvar = findViewById<MaterialButton>(R.id.btnSalvar)

        toolbar.setNavigationOnClickListener { finish() }

        spnUnidade.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, Unidade.entries)
        spnCategoria.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, Categoria.entries)

        btnSalvar.setOnClickListener {
            val nome = tilNome.editText?.text?.toString()?.trim().orEmpty()
            val qtdStr = tilQuantidade.editText?.text?.toString()?.replace(",", ".")
            val qtd = qtdStr?.toDoubleOrNull() ?: 0.0

            var valido = true
            if (nome.isBlank()) { tilNome.error = getString(R.string.erro_campo_obrigatorio); valido = false } else { tilNome.error = null }
            if (qtd <= 0) { tilQuantidade.error = getString(R.string.erro_campo_obrigatorio); valido = false } else { tilQuantidade.error = null }
            if (!valido) return@setOnClickListener

            val unidade = Unidade.entries[spnUnidade.selectedItemPosition]
            val categoria = Categoria.entries[spnCategoria.selectedItemPosition]

            val data = Intent().apply {
                putExtra(EXTRA_NOME, nome)
                putExtra(EXTRA_QTD, qtd)
                putExtra(EXTRA_UNIDADE, unidade.name)
                putExtra(EXTRA_CATEGORIA, categoria.name)
            }
            setResult(RESULT_OK, data)
            finish()
        }
    }
}
