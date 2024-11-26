package com.example.tasky.auth.mock

import com.example.tasky.auth.data.model.AccessTokenBody
import com.example.tasky.auth.data.model.LoginResponse
import io.ktor.client.plugins.auth.providers.BearerTokens
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.coroutines.cancellation.CancellationException

object SessionManagerMock {
    private var loginResponseJson: String? = null
    private var json = Json

    private suspend fun loadLoginResponse(): LoginResponse? =
        try {
            val loginResponseJsonString = loginResponseJson

            loginResponseJsonString?.let {
                json.decodeFromString<LoginResponse>(
                    it,
                )
            }
        } catch (e: Exception) {
            if (e is CancellationException) throw e

            println("no loginResponse: $e")
            loginResponseJson = null
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

    suspend fun loadAccessTokenBody(): AccessTokenBody? =
        try {
            val loginResponse = loadLoginResponse() ?: throw Exception("no login response")
            AccessTokenBody(refreshToken = loginResponse.refreshToken, loginResponse.userId)
        } catch (e: Exception) {
            if (e is CancellationException) throw e

            println("refresh token fail: $e")
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

            loginResponseJson = jsonString
        } catch (e: Exception) {
            if (e is CancellationException) throw e

            println("update token fail: $e")
        }
    }

    suspend fun removeToken() {
        loginResponseJson = null
    }

    suspend fun getFullName(): String? =
        try {
            val loginResponse = loadLoginResponse()

            loginResponse?.fullName
        } catch (e: Exception) {
            if (e is CancellationException) throw e

            println("cannot get name: $e")
            null
        }

    suspend fun getUserId(): String? =
        try {
            val loginResponse = loadLoginResponse()

            loginResponse?.userId
        } catch (e: Exception) {
            if (e is CancellationException) throw e

            println("cannot get userId: $e")
            null
        }
}
