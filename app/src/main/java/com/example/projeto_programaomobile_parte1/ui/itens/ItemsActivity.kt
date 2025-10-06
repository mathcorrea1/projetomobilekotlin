package com.example.projeto_programaomobile_parte1.ui.itens

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projeto_programaomobile_parte1.R
import com.example.projeto_programaomobile_parte1.data.model.Categoria
import com.example.projeto_programaomobile_parte1.data.model.Item
import com.example.projeto_programaomobile_parte1.data.model.Unidade
import com.example.projeto_programaomobile_parte1.databinding.ActivityItemsBinding
import com.example.projeto_programaomobile_parte1.domain.AgrupamentoItens
import com.example.projeto_programaomobile_parte1.viewmodel.ItemsViewModel
import com.google.android.material.snackbar.Snackbar

class ItemsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityItemsBinding
    private lateinit var vm: ItemsViewModel

    private val novoItemLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data
            val nome = data?.getStringExtra(AddItemActivity.EXTRA_NOME).orEmpty()
            val qtd = data?.getDoubleExtra(AddItemActivity.EXTRA_QTD, 0.0) ?: 0.0
            val undName = data?.getStringExtra(AddItemActivity.EXTRA_UNIDADE)
            val catName = data?.getStringExtra(AddItemActivity.EXTRA_CATEGORIA)
            if (nome.isNotBlank() && qtd > 0 && undName != null && catName != null) {
                val und = runCatching { Unidade.valueOf(undName) }.getOrNull()
                val cat = runCatching { Categoria.valueOf(catName) }.getOrNull()
                if (und != null && cat != null) vm.adicionarItem(nome, qtd, und, cat)
            }
        }
    }

    private val editarItemLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data ?: return@registerForActivityResult
            val excluir = data.getBooleanExtra(EditItemActivity.EXTRA_DELETE, false)
            val id = data.getStringExtra(EditItemActivity.EXTRA_ID).orEmpty()
            if (excluir) {
                if (id.isNotBlank()) vm.removerItem(id)
            } else {
                val nome = data.getStringExtra(EditItemActivity.EXTRA_NOME).orEmpty()
                val qtd = data.getDoubleExtra(EditItemActivity.EXTRA_QTD, 0.0)
                val undName = data.getStringExtra(EditItemActivity.EXTRA_UNIDADE)
                val catName = data.getStringExtra(EditItemActivity.EXTRA_CATEGORIA)
                val comprado = data.getBooleanExtra(EditItemActivity.EXTRA_COMPRADO, false)
                val listaIdAtual = intent.getStringExtra("listaId").orEmpty()
                if (id.isNotBlank() && nome.isNotBlank() && qtd > 0 && undName != null && catName != null) {
                    val und = runCatching { Unidade.valueOf(undName) }.getOrNull()
                    val cat = runCatching { Categoria.valueOf(catName) }.getOrNull()
                    if (und != null && cat != null) {
                        val itemAtualizado = Item(
                            id = id,
                            listaId = listaIdAtual,
                            nome = nome,
                            quantidade = qtd,
                            unidade = und,
                            categoria = cat,
                            comprado = comprado
                        )
                        vm.editarItem(itemAtualizado)
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityItemsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }

        val listaId = intent.getStringExtra("listaId") ?: error("listaId obrigatório")
        title = intent.getStringExtra("listaTitulo") ?: getString(R.string.titulo_itens)

        vm = ViewModelProvider(this, ItemsVMFactory(listaId))[ItemsViewModel::class.java]

        val adapter = ItemAdapter(
            onEditar = { item -> abrirTelaEditarItem(item) },
            onExcluir = { item -> vm.removerItem(item.id) },
            onAlternarComprado = { item -> vm.alternarComprado(item) }
        )
        binding.rvItens.layoutManager = LinearLayoutManager(this)
        binding.rvItens.adapter = adapter

        vm.agrupados.observe(this) { agrupar -> adapter.submitList(montarLinhas(agrupar)) }
        vm.mensagem.observe(this) { msg -> msg?.let { Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show() } }

        binding.fabAdd.setOnClickListener {
            val i = Intent(this, AddItemActivity::class.java)
            novoItemLauncher.launch(i)
        }

        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean { vm.atualizarBusca(query.orEmpty()); return true }
            override fun onQueryTextChange(newText: String?): Boolean { vm.atualizarBusca(newText.orEmpty()); return true }
        })
    }

    private fun abrirTelaEditarItem(item: Item) {
        val i = Intent(this, EditItemActivity::class.java).apply {
            putExtra(EditItemActivity.EXTRA_ID, item.id)
            putExtra(EditItemActivity.EXTRA_LISTA_ID, item.listaId)
            putExtra(EditItemActivity.EXTRA_NOME, item.nome)
            putExtra(EditItemActivity.EXTRA_QTD, item.quantidade)
            putExtra(EditItemActivity.EXTRA_UNIDADE, item.unidade.name)
            putExtra(EditItemActivity.EXTRA_CATEGORIA, item.categoria.name)
            putExtra(EditItemActivity.EXTRA_COMPRADO, item.comprado)
        }
        editarItemLauncher.launch(i)
    }

    private fun montarLinhas(ag: AgrupamentoItens): List<LinhaItem> {
        val linhas = mutableListOf<LinhaItem>()
        if (ag.aComprar.isNotEmpty()) {
            ag.aComprar.forEach { grupo ->
                linhas += LinhaItem.Header(grupo.categoria.titulo)
                grupo.itens.forEach { linhas += LinhaItem.ItemLinha(it) }
            }
        }
        if (ag.comprados.isNotEmpty()) {
            linhas += LinhaItem.Header(getString(R.string.secao_comprados))
            ag.comprados.forEach { grupo ->
                linhas += LinhaItem.Header(grupo.categoria.titulo)
                grupo.itens.forEach { linhas += LinhaItem.ItemLinha(it) }
            }
        }
        return linhas
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> { finish(); true }
            R.id.acao_logout -> { finish(); true }
            else -> super.onOptionsItemSelected(item)
        }
    }
}

class ItemsVMFactory(private val listaId: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ItemsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ItemsViewModel(listaId) as T
        }
        throw IllegalArgumentException("Unknown VM class")
    }
}
