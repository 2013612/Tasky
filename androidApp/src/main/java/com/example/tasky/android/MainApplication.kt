package com.example.tasky.android

import android.app.Application
import com.example.tasky.dataStore.createSettings
import com.example.tasky.getDataStore
import com.example.tasky.manager.LoginManager
import com.example.tasky.manager.loginManager
import com.example.tasky.repository.LoginRepository
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ExperimentalSettingsImplementation

class MainApplication : Application() {
    @OptIn(ExperimentalSettingsApi::class, ExperimentalSettingsImplementation::class)
    override fun onCreate() {
        super.onCreate()
        loginManager = LoginManager(createSettings(getDataStore(this)), LoginRepository())
    }
}
