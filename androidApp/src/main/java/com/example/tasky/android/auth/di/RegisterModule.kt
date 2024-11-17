package com.example.tasky.android.auth.di

import com.example.tasky.android.auth.viewmodel.RegisterViewModel
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
