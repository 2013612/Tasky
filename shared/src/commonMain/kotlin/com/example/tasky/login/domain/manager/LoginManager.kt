package com.example.tasky.login.domain.manager

import com.example.tasky.common.data.manager.HttpManager
import com.example.tasky.common.domain.model.ResultWrapper
import com.example.tasky.common.domain.model.onSuccess
import com.example.tasky.dataStore.SettingsKey
import com.example.tasky.login.domain.ILoginRepository
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.coroutines.FlowSettings
import kotlinx.serialization.encodeToString

@OptIn(ExperimentalSettingsApi::class)
class LoginManager(
    private val settings: FlowSettings,
    private val loginRepository: ILoginRepository,
) {
    suspend fun login(
        email: String,
        password: String,
    ): Boolean {
        val result =
            loginRepository
                .login(email, password)
                .onSuccess {
                    val jsonString = HttpManager.json.encodeToString(it)
                    settings.putString(SettingsKey.LOGIN_RESPONSE.name, jsonString)
                }

        return result is ResultWrapper.Success
    }

    suspend fun logOut() {
        loginRepository.logout()
        settings.remove(SettingsKey.LOGIN_RESPONSE.name)
    }
}
