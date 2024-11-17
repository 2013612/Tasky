package com.example.tasky.android.login.di

import com.example.tasky.android.login.viewmodel.RegisterViewModel
import com.example.tasky.auth.data.AuthRepository
import com.example.tasky.auth.domain.IAuthRepository
import com.russhwolf.settings.ExperimentalSettingsApi
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

@OptIn(ExperimentalSettingsApi::class)
val registerModule =
    module {
        singleOf<AuthRepository>(::AuthRepository).bind<IAuthRepository>()
        viewModelOf(::RegisterViewModel)
    }
