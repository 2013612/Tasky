package com.example.tasky.auth.data

import com.example.tasky.auth.data.model.LoginBody
import com.example.tasky.auth.data.model.RegisterBody
import com.example.tasky.auth.domain.IAuthRepository
import com.example.tasky.common.data.manager.HttpManager
import com.example.tasky.common.data.model.DataError
import com.example.tasky.common.domain.model.ResultWrapper
import com.example.tasky.common.domain.model.map
import com.example.tasky.common.domain.model.onSuccess
import com.example.tasky.dataStore.SettingsKey
import com.example.tasky.dataStore.createSettings
import com.example.tasky.dataStore.dataStore
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ExperimentalSettingsImplementation
import com.russhwolf.settings.coroutines.FlowSettings
import kotlinx.serialization.encodeToString

@OptIn(ExperimentalSettingsApi::class, ExperimentalSettingsImplementation::class)
class AuthRepository(
    private val authDataSource: AuthDataSource = AuthDataSource(),
    private val settings: FlowSettings = createSettings(dataStore),
) : IAuthRepository {
    override suspend fun login(
        email: String,
        password: String,
    ): ResultWrapper<Boolean, DataError.Remote> =
        authDataSource
            .login(LoginBody(email, password))
            .onSuccess {
                val jsonString = HttpManager.json.encodeToString(it)
                settings.putString(SettingsKey.LOGIN_RESPONSE.name, jsonString)
            }.map { true }

    override suspend fun logout(): ResultWrapper<Unit, DataError.Remote> =
        authDataSource.logout().onSuccess {
            settings.remove(SettingsKey.LOGIN_RESPONSE.name)
        }

    override suspend fun register(
        fullName: String,
        email: String,
        password: String,
    ) = authDataSource.register(
        RegisterBody(fullName, email, password),
    )
}