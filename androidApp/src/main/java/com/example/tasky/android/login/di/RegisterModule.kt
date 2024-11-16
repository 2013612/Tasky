package com.example.tasky.android.login.di

import com.example.tasky.android.login.viewmodel.RegisterViewModel
import com.example.tasky.login.data.LoginRepository
import com.example.tasky.login.domain.ILoginRepository
import com.russhwolf.settings.ExperimentalSettingsApi
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

@OptIn(ExperimentalSettingsApi::class)
val registerModule =
    module {
        singleOf<LoginRepository>(::LoginRepository).bind<ILoginRepository>()
        viewModelOf(::RegisterViewModel)
    }
