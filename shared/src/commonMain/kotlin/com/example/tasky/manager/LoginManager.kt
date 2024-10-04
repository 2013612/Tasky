package com.example.tasky.manager

import com.example.tasky.dataStore.SettingsKey
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.coroutines.FlowSettings
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest

@OptIn(ExperimentalSettingsApi::class)
class LoginManager(
    private val settings: FlowSettings,
) {
    private val tokenFlow: Flow<String?> = settings.getStringOrNullFlow(SettingsKey.TOKEN.name)

    @OptIn(ExperimentalCoroutinesApi::class)
    val isLoginInFlow = tokenFlow.mapLatest { !it.isNullOrEmpty() }

    suspend fun logIn(): Boolean {
        delay(1000)
        settings.putString(SettingsKey.TOKEN.name, "token")

        return true
    }

    suspend fun logOut() {
        delay(1000)
        settings.remove(SettingsKey.TOKEN.name)
    }
}
