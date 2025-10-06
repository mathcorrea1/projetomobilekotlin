package com.example.projeto_programaomobile_parte1.ui.listas

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.projeto_programaomobile_parte1.R
import com.example.projeto_programaomobile_parte1.data.model.ListaCompra
import com.example.projeto_programaomobile_parte1.databinding.ActivityListsBinding
import com.example.projeto_programaomobile_parte1.di.ServiceLocator
import com.example.projeto_programaomobile_parte1.ui.login.LoginActivity
import com.example.projeto_programaomobile_parte1.viewmodel.ListsViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import java.io.File

class ListsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListsBinding
    private lateinit var vm: ListsViewModel

    private var listaSelecionadaParaImagem: ListaCompra? = null
    private var imagemCameraUri: Uri? = null

    private val abrirDocumento = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
        val lista = listaSelecionadaParaImagem
        if (uri != null && lista != null) {
            vm.editarLista(lista.id, lista.titulo, uri)
        }
        listaSelecionadaParaImagem = null
    }

    private val tirarFoto = registerForActivityResult(ActivityResultContracts.TakePicture()) { sucesso: Boolean ->
        val lista = listaSelecionadaParaImagem
        if (sucesso && imagemCameraUri != null && lista != null) {
            vm.editarLista(lista.id, lista.titulo, imagemCameraUri)
        }
        listaSelecionadaParaImagem = null
        imagemCameraUri = null
    }

    private val novaListaLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data
            val titulo = data?.getStringExtra(AddListActivity.EXTRA_TITULO).orEmpty()
            val imagemStr = data?.getStringExtra(AddListActivity.EXTRA_IMAGEM)
            val imagemUri = imagemStr?.let { Uri.parse(it) }
            if (titulo.isNotBlank()) {
                vm.adicionarLista(titulo, imagemUri)
            }
        }
    }

    private val editarListaLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data
            val id = data?.getStringExtra(EditListActivity.EXTRA_ID).orEmpty()
            val excluir = data?.getBooleanExtra(EditListActivity.EXTRA_DELETE, false) == true
            if (excluir) {
                if (id.isNotBlank()) vm.excluirLista(id)
            } else {
                val titulo = data?.getStringExtra(EditListActivity.EXTRA_TITULO).orEmpty()
                val imagemStr = data?.getStringExtra(EditListActivity.EXTRA_IMAGEM)
                val imagemUri = imagemStr?.let { Uri.parse(it) }
                if (id.isNotBlank() && titulo.isNotBlank()) {
                    vm.editarLista(id, titulo, imagemUri)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        vm = ViewModelProvider(this)[ListsViewModel::class.java]

        val adapter = ListaAdapter(
            aoClicar = { lista, _ -> abrirItensDaLista(lista) },
            aoLongo = { lista, view -> mostrarMenuLista(lista, view) }
        )
        binding.rvListas.layoutManager = GridLayoutManager(this, 2)
        binding.rvListas.adapter = adapter

        binding.fabAdd.setOnClickListener {
            val i = Intent(this, AddListActivity::class.java)
            novaListaLauncher.launch(i)
        }

        binding.btnLogout.setOnClickListener { realizarLogout() }

        vm.listasFiltradas.observe(this) { listas ->
            adapter.submitList(listas)
            binding.txtVazio.visibility = if (listas.isEmpty()) View.VISIBLE else View.GONE
        }
        vm.mensagem.observe(this) { msg -> msg?.let { Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show() } }

        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean { vm.atualizarBusca(query.orEmpty()); return true }
            override fun onQueryTextChange(newText: String?): Boolean { vm.atualizarBusca(newText.orEmpty()); return true }
        })
    }

    private fun realizarLogout() {
        // Descarta dados
        ServiceLocator.listRepository().limpar()
        ServiceLocator.authRepository().efetuarLogout()
        // Volta para a tela de login e limpa
        val i = Intent(this, LoginActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
        startActivity(i)
    }

    private fun mostrarMenuLista(lista: ListaCompra, anchor: View) {
        val menu = PopupMenu(this, anchor)
        menu.menuInflater.inflate(R.menu.menu_lista_item, menu.menu)
        menu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_editar -> { abrirTelaEditarLista(lista); true }
                R.id.menu_excluir -> { vm.excluirLista(lista.id); true }
                R.id.menu_imagem -> { selecionarImagemPara(lista); true }
                else -> false
            }
        }
        menu.show()
    }

    private fun abrirTelaEditarLista(lista: ListaCompra) {
        val i = Intent(this, EditListActivity::class.java).apply {
            putExtra(EditListActivity.EXTRA_ID, lista.id)
            putExtra(EditListActivity.EXTRA_TITULO, lista.titulo)
            putExtra(EditListActivity.EXTRA_IMAGEM, lista.imagemUri?.toString())
        }
        editarListaLauncher.launch(i)
    }

    private fun abrirItensDaLista(lista: ListaCompra) {
        val i = Intent(this, com.example.projeto_programaomobile_parte1.ui.itens.ItemsActivity::class.java).apply {
            putExtra("listaId", lista.id)
            putExtra("listaTitulo", lista.titulo)
        }
        startActivity(i)
    }

    private fun selecionarImagemPara(lista: ListaCompra) {
        listaSelecionadaParaImagem = lista
        mostrarDialogoEscolhaImagem()
    }

    private fun mostrarDialogoEscolhaImagem() {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.titulo_escolher_origem_imagem)
            .setItems(arrayOf(
                getString(R.string.opcao_camera),
                getString(R.string.opcao_galeria)
            )) { _, which ->
                when (which) {
                    0 -> abrirCamera()
                    1 -> abrirGaleria()
                }
            }
            .show()
    }

    private fun abrirCamera() {
        val imagemArquivo = File(cacheDir, "foto_${System.currentTimeMillis()}.jpg")
        val uri = FileProvider.getUriForFile(
            this,
            "${packageName}.fileprovider",
            imagemArquivo
        )
        imagemCameraUri = uri
        tirarFoto.launch(uri)
    }

    private fun abrirGaleria() {
        abrirDocumento.launch(arrayOf("image/*"))
    }
}
