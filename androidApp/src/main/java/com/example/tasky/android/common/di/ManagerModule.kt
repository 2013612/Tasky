package com.example.tasky.android.common.di

import com.example.tasky.login.domain.manager.LoginManager
import com.russhwolf.settings.ExperimentalSettingsApi
import org.koin.dsl.module

@OptIn(ExperimentalSettingsApi::class)
val managerModule =
    module {
        single<LoginManager> {
            LoginManager(get(), get())
        }
    }
