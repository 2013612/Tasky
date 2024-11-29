package com.example.tasky.android.auth.di

import com.example.tasky.android.auth.presentation.register.screen.RegisterViewModel
import com.example.tasky.auth.data.AuthRepository
import com.example.tasky.auth.domain.IAuthRepository
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val registerModule =
    module {
        singleOf<AuthRepository>(::AuthRepository).bind<IAuthRepository>()
        viewModelOf(::RegisterViewModel)
    }
