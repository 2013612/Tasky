package com.example.tasky.android.login.di

import com.example.tasky.android.login.viewmodel.LoginViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val loginModule =
    module {
        viewModelOf(::LoginViewModel)
    }
