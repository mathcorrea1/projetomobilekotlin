package com.example.projeto_programaomobile_parte1.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.projeto_programaomobile_parte1.databinding.ActivityLoginBinding
import com.example.projeto_programaomobile_parte1.ui.listas.ListsActivity
import com.example.projeto_programaomobile_parte1.ui.register.RegisterActivity
import com.google.android.material.snackbar.Snackbar
import com.example.projeto_programaomobile_parte1.viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var vm: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        vm = ViewModelProvider(this)[LoginViewModel::class.java]

        binding.btnEntrar.setOnClickListener {
            val email = binding.inputEmail.editText?.text?.toString()?.trim().orElse("")
            val senha = binding.inputSenha.editText?.text?.toString()?.trim().orElse("")
            vm.efetuarLogin(email, senha)
        }
        binding.linkCadastrar.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        vm.erro.observe(this) { msg -> msg?.let { Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show() } }
        vm.sucesso.observe(this) { ok -> if (ok == true) {
            startActivity(Intent(this, ListsActivity::class.java))
            finish()
        }}
    }
}

private fun String?.orElse(alt: String) = this ?: alt

