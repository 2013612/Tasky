package com.example.tasky.android.auth.di

import com.example.tasky.android.auth.presentation.login.screen.LoginViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val loginModule =
    module {
        viewModelOf(::LoginViewModel)
    }
