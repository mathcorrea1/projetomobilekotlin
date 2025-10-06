package com.example.projeto_programaomobile_parte1.ui.listas

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.example.projeto_programaomobile_parte1.R
import com.example.projeto_programaomobile_parte1.databinding.ActivityAddListBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.io.File

class AddListActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_TITULO = "titulo"
        const val EXTRA_IMAGEM = "imagemUri"
    }

    private lateinit var binding: ActivityAddListBinding
    private var imagemSelecionada: Uri? = null
    private var imagemCameraUri: Uri? = null

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
        binding = ActivityAddListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { finish() }

        binding.btnEscolherImagem.setOnClickListener {
            mostrarDialogoEscolhaImagem()
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
