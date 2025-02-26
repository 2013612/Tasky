package com.example.tasky.common.di

import com.example.tasky.auth.data.AuthRepository
import com.example.tasky.auth.domain.IAuthRepository
import org.koin.dsl.module

val repositoryModule =
    module {
        single<IAuthRepository> {
            AuthRepository()
        }
    }
