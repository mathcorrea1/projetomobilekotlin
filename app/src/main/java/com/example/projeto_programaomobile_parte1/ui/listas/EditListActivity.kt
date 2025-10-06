package com.example.projeto_programaomobile_parte1.ui.listas

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.example.projeto_programaomobile_parte1.R
import com.example.projeto_programaomobile_parte1.databinding.ActivityEditListBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.io.File

class EditListActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_ID = "id"
        const val EXTRA_TITULO = "titulo"
        const val EXTRA_IMAGEM = "imagemUri"
        const val EXTRA_DELETE = "excluir"
    }

    private lateinit var binding: ActivityEditListBinding
    private var imagemSelecionada: Uri? = null
    private var imagemCameraUri: Uri? = null
    private var listaId: String = ""

    private val abrirDocumento = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
        if (uri != null) {
            imagemSelecionada = uri
            binding.imgPreview.setImageURI(uri)
        }
    }

    private val tirarFoto = registerForActivityResult(ActivityResultContracts.TakePicture()) { sucesso: Boolean ->
        if (sucesso && imagemCameraUri != null) {
            imagemSelecionada = imagemCameraUri
            binding.imgPreview.setImageURI(imagemCameraUri)
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
            mostrarDialogoEscolhaImagem()
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
