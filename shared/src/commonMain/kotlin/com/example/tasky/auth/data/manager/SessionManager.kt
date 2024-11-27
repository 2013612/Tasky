package com.example.tasky.auth.data.manager

import com.example.tasky.auth.data.model.LoginResponse
import com.example.tasky.auth.domain.ISessionManager
import com.example.tasky.auth.domain.model.RefreshToken
import com.example.tasky.common.data.manager.HttpManager.json
import com.example.tasky.dataStore.SettingsKey
import com.example.tasky.dataStore.createSettings
import com.example.tasky.dataStore.dataStore
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ExperimentalSettingsImplementation
import io.ktor.client.plugins.auth.providers.BearerTokens
import kotlinx.serialization.encodeToString
import kotlin.coroutines.cancellation.CancellationException

@OptIn(ExperimentalSettingsImplementation::class, ExperimentalSettingsApi::class)
object SessionManager : ISessionManager {
    private val settings = createSettings(dataStore)

    private suspend fun loadLoginResponse(): LoginResponse? =
        try {
            val loginResponseJsonString =
                settings.getStringOrNull(SettingsKey.LOGIN_RESPONSE.name)

            loginResponseJsonString?.let {
                json.decodeFromString<LoginResponse>(
                    it,
                )
            }
        } catch (e: Exception) {
            if (e is CancellationException) throw e

            println("no loginResponse: $e")
            settings.remove(SettingsKey.LOGIN_RESPONSE.name)
            null
        }

    suspend fun loadTokens(): BearerTokens =
        try {
            val loginResponse = loadLoginResponse()
            val accessToken = loginResponse?.accessToken ?: ""
            val refreshToken = loginResponse?.refreshToken ?: ""

            BearerTokens(accessToken, refreshToken)
        } catch (e: Exception) {
            if (e is CancellationException) throw e

            println("cannot load tokens: $e")
            BearerTokens("", "")
        }

    suspend fun loadAccessTokenBody(): RefreshToken? =
        try {
            val loginResponse = loadLoginResponse() ?: throw Exception("no login response")
            RefreshToken(refreshToken = loginResponse.refreshToken, loginResponse.userId)
        } catch (e: Exception) {
            if (e is CancellationException) throw e

            println("load refresh token fail: $e")
            null
        }

    suspend fun updateAccessToken(
        newToken: String,
        expirationTimestamp: Long,
    ) {
        try {
            val loginResponse = loadLoginResponse() ?: throw Exception("no login response")
            val jsonString =
                json.encodeToString(
                    loginResponse.copy(
                        accessToken = newToken,
                        accessTokenExpirationTimestamp = expirationTimestamp,
                    ),
                )

            settings.putString(SettingsKey.LOGIN_RESPONSE.name, jsonString)
        } catch (e: Exception) {
            if (e is CancellationException) throw e

            println("update token fail: $e")
        }
    }

    suspend fun removeToken() {
        settings.remove(SettingsKey.LOGIN_RESPONSE.name)
    }

    override suspend fun getFullName(): String? =
        try {
            val loginResponse = loadLoginResponse()

            loginResponse?.fullName
        } catch (e: Exception) {
            if (e is CancellationException) throw e

            println("cannot get name: $e")
            null
        }

    override suspend fun getUserId(): String? =
        try {
            val loginResponse = loadLoginResponse()

            loginResponse?.userId
        } catch (e: Exception) {
            if (e is CancellationException) throw e

            println("cannot get userId: $e")
            null
        }
}
