package com.example.projeto_programaomobile_parte1.ui.listas

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.projeto_programaomobile_parte1.databinding.ActivityAddListBinding

class AddListActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_TITULO = "titulo"
        const val EXTRA_IMAGEM = "imagemUri"
    }

    private lateinit var binding: ActivityAddListBinding
    private var imagemSelecionada: Uri? = null

    private val abrirDocumento = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
        if (uri != null) {
            imagemSelecionada = uri
            binding.imgPreview.setImageURI(uri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { finish() }

        binding.btnEscolherImagem.setOnClickListener {
            abrirDocumento.launch(arrayOf("image/*"))
        }

        binding.btnAdicionar.setOnClickListener {
            val titulo = binding.tilTitulo.editText?.text?.toString()?.trim().orEmpty()
            if (titulo.isBlank()) {
                binding.tilTitulo.error = "Campo obrigatório"
                return@setOnClickListener
            }
            binding.tilTitulo.error = null
            val data = Intent()
            data.putExtra(EXTRA_TITULO, titulo)
            data.putExtra(EXTRA_IMAGEM, imagemSelecionada?.toString())
            setResult(RESULT_OK, data)
            finish()
        }
    }
}
