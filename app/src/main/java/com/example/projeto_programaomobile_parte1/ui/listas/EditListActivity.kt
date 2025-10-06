package com.example.projeto_programaomobile_parte1.ui.listas

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.projeto_programaomobile_parte1.databinding.ActivityEditListBinding

class EditListActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_ID = "id"
        const val EXTRA_TITULO = "titulo"
        const val EXTRA_IMAGEM = "imagemUri"
        const val EXTRA_DELETE = "excluir"
    }

    private lateinit var binding: ActivityEditListBinding
    private var imagemSelecionada: Uri? = null
    private var listaId: String = ""

    private val abrirDocumento = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
        if (uri != null) {
            imagemSelecionada = uri
            binding.imgPreview.setImageURI(uri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityEditListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        listaId = intent.getStringExtra(EXTRA_ID).orEmpty()
        val tituloInicial = intent.getStringExtra(EXTRA_TITULO).orEmpty()
        val imagemInicial = intent.getStringExtra(EXTRA_IMAGEM)?.let { Uri.parse(it) }
        imagemSelecionada = imagemInicial

        binding.toolbar.setNavigationOnClickListener { finish() }

        binding.tilTitulo.editText?.setText(tituloInicial)
        imagemInicial?.let { binding.imgPreview.setImageURI(it) }

        binding.btnEscolherImagem.setOnClickListener {
            abrirDocumento.launch(arrayOf("image/*"))
        }

        binding.btnSalvar.setOnClickListener {
            val novoTitulo = binding.tilTitulo.editText?.text?.toString()?.trim().orEmpty()
            if (novoTitulo.isBlank()) {
                binding.tilTitulo.error = "Campo obrigatório"
                return@setOnClickListener
            }
            binding.tilTitulo.error = null
            val data = Intent()
            data.putExtra(EXTRA_ID, listaId)
            data.putExtra(EXTRA_TITULO, novoTitulo)
            data.putExtra(EXTRA_IMAGEM, imagemSelecionada?.toString())
            setResult(RESULT_OK, data)
            finish()
        }

        binding.btnExcluir.setOnClickListener {
            val data = Intent()
            data.putExtra(EXTRA_ID, listaId)
            data.putExtra(EXTRA_DELETE, true)
            setResult(RESULT_OK, data)
            finish()
        }
    }
}
