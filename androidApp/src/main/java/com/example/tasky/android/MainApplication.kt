package com.example.tasky.android

import android.app.Application
import com.example.tasky.android.agenda.di.agendaModule
import com.example.tasky.android.login.di.registerModule
import com.example.tasky.dataStore.createSettings
import com.example.tasky.getDataStore
import com.example.tasky.manager.LoginManager
import com.example.tasky.manager.loginManager
import com.example.tasky.repository.LoginRepository
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ExperimentalSettingsImplementation
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainApplication : Application() {
    @OptIn(ExperimentalSettingsApi::class, ExperimentalSettingsImplementation::class)
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(
                agendaModule,
            )
            modules(registerModule)
        }
        loginManager = LoginManager(createSettings(getDataStore(this)), LoginRepository())
    }
}
