package com.example.tasky.login.domain

import com.example.tasky.common.manager.HttpManager
import com.example.tasky.common.model.ResultWrapper
import com.example.tasky.common.model.onSuccess
import com.example.tasky.dataStore.SettingsKey
import com.example.tasky.login.data.model.LoginBody
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.coroutines.FlowSettings
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.serialization.encodeToString

lateinit var loginManager: LoginManager

@OptIn(ExperimentalSettingsApi::class)
class LoginManager(
    private val settings: FlowSettings,
    private val loginRepository: ILoginRepository,
) {
    private val loginResponseFlow: Flow<String?> = settings.getStringOrNullFlow(SettingsKey.LOGIN_RESPONSE.name)

    @OptIn(ExperimentalCoroutinesApi::class)
    val isLoginFlow = loginResponseFlow.mapLatest { !it.isNullOrEmpty() }

    suspend fun logIn(loginBody: LoginBody): Boolean {
        val result =
            loginRepository
                .login(loginBody)
                .onSuccess {
                    val jsonString = HttpManager.json.encodeToString(it)
                    settings.putString(SettingsKey.LOGIN_RESPONSE.name, jsonString)
                }

        return result is ResultWrapper.Success
    }

    suspend fun logOut() {
        delay(1000)
        settings.remove(SettingsKey.LOGIN_RESPONSE.name)
    }
}
