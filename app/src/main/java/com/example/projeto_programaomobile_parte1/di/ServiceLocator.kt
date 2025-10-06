package com.example.projeto_programaomobile_parte1.di

import com.example.projeto_programaomobile_parte1.data.repo.AuthRepository
import com.example.projeto_programaomobile_parte1.data.repo.api.IAuthRepository
import com.example.projeto_programaomobile_parte1.data.repo.api.IListRepository

object ServiceLocator {
    fun authRepository(): IAuthRepository = AuthRepository
    fun listRepository(): IListRepository = com.example.projeto_programaomobile_parte1.data.repo.ListRepository
}
