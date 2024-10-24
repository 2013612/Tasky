package com.example.tasky.android.login.di

import com.example.tasky.android.login.viewmodel.RegisterViewModel
import com.example.tasky.repository.ILoginRepository
import com.example.tasky.repository.LoginRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val registerModule =
    module {
        singleOf<LoginRepository>(::LoginRepository) {
            bind<ILoginRepository>()
        }
        viewModelOf(::RegisterViewModel)
    }
