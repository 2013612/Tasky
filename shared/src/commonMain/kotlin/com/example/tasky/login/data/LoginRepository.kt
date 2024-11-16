package com.example.tasky.login.data

import com.example.tasky.common.data.manager.HttpManager
import com.example.tasky.common.data.model.BaseError
import com.example.tasky.common.domain.model.ResultWrapper
import com.example.tasky.common.domain.model.map
import com.example.tasky.common.domain.model.onSuccess
import com.example.tasky.dataStore.SettingsKey
import com.example.tasky.dataStore.createSettings
import com.example.tasky.dataStore.dataStore
import com.example.tasky.login.data.model.LoginBody
import com.example.tasky.login.data.model.RegisterBody
import com.example.tasky.login.domain.ILoginRepository
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ExperimentalSettingsImplementation
import com.russhwolf.settings.coroutines.FlowSettings
import kotlinx.serialization.encodeToString

@OptIn(ExperimentalSettingsApi::class, ExperimentalSettingsImplementation::class)
class LoginRepository(
    private val loginDataSource: LoginDataSource = LoginDataSource(),
    private val settings: FlowSettings = createSettings(dataStore),
) : ILoginRepository {
    override suspend fun login(
        email: String,
        password: String,
    ): ResultWrapper<Boolean, BaseError> =
        loginDataSource
            .login(LoginBody(email, password))
            .onSuccess {
                val jsonString = HttpManager.json.encodeToString(it)
                settings.putString(SettingsKey.LOGIN_RESPONSE.name, jsonString)
            }.map { true }

    override suspend fun logout(): ResultWrapper<Unit, BaseError> =
        loginDataSource.logout().onSuccess {
            settings.remove(SettingsKey.LOGIN_RESPONSE.name)
        }

    override suspend fun register(
        fullName: String,
        email: String,
        password: String,
    ) = loginDataSource.register(
        RegisterBody(fullName, email, password),
    )
}
