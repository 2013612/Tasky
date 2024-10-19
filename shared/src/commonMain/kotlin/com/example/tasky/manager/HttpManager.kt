package com.example.tasky.manager

import com.example.tasky.BuildKonfig.API_KEY
import com.example.tasky.dataStore.SettingsKey
import com.example.tasky.dataStore.createSettings
import com.example.tasky.dataStore.getDataStore
import com.example.tasky.model.ErrorResponse
import com.example.tasky.model.login.AccessToken
import com.example.tasky.model.login.AccessTokenBody
import com.example.tasky.model.login.AccessTokenResponse
import com.example.tasky.model.login.LoginResponse
import com.example.tasky.util.isSuccess
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ExperimentalSettingsImplementation
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.resources.Resources
import io.ktor.client.plugins.resources.post
import io.ktor.client.request.setBody
import io.ktor.http.URLProtocol
import io.ktor.http.headers
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

internal object HttpManager {
    private const val TIMEOUT = 60_000L
    private val json =
        Json {
            prettyPrint
        }

    @OptIn(ExperimentalSettingsImplementation::class, ExperimentalSettingsApi::class)
    val httpClient =
        HttpClient {
            install(Resources)
            install(HttpTimeout) {
                connectTimeoutMillis = TIMEOUT
                requestTimeoutMillis = TIMEOUT
            }
            install(ContentNegotiation) {
                json(
                    json,
                )
            }
            install(Logging) {
                logger =
                    object : Logger {
                        override fun log(message: String) {
                            println("HTTP call $message")
                        }
                    }
                level = LogLevel.ALL
            }

            install(Auth) {
                bearer {
                    loadTokens {
                        var accessToken: String
                        var refreshToken: String
                        try {
                            val loginResponseJsonString =
                                createSettings(
                                    getDataStore { "" },
                                ).getStringOrNull(SettingsKey.LOGIN_RESPONSE.name)
                            val loginResponse =
                                loginResponseJsonString?.let {
                                    json.decodeFromString<LoginResponse>(
                                        it,
                                    )
                                }
                            accessToken = loginResponse?.accessToken ?: ""
                            refreshToken = loginResponse?.refreshToken ?: ""
                        } catch (e: Exception) {
                            println("cannot load tokens: $e")
                            accessToken = ""
                            refreshToken = ""
                        }

                        BearerTokens(accessToken, refreshToken)
                    }

                    refreshTokens {
                        val settings =
                            createSettings(
                                getDataStore { "" },
                            )

                        try {
                            val loginResponseJsonString =
                                settings.getStringOrNull(SettingsKey.LOGIN_RESPONSE.name)
                            val loginResponse =
                                loginResponseJsonString?.let {
                                    json.decodeFromString<LoginResponse>(
                                        it,
                                    )
                                } ?: throw Exception("no login response")
                            val body = AccessTokenBody(refreshToken = loginResponse.refreshToken, loginResponse.userId)
                            val response =
                                client.post(AccessToken()) {
                                    markAsRefreshTokenRequest()
                                    setBody(body)
                                }

                            if (!response.isSuccess()) {
                                val errorResponse = response.body<ErrorResponse>()
                                println("refresh token fail: ${errorResponse.message}")
                                throw Exception(errorResponse.message)
                            }

                            val responseBody = response.body<AccessTokenResponse>()

                            val jsonString =
                                json.encodeToString(
                                    loginResponse.copy(
                                        accessToken = responseBody.accessToken,
                                        accessTokenExpirationTimestamp = responseBody.expirationTimestamp,
                                    ),
                                )

                            settings.putString(SettingsKey.LOGIN_RESPONSE.name, jsonString)

                            BearerTokens(responseBody.accessToken, loginResponse.refreshToken)
                        } catch (e: Exception) {
                            settings.remove(SettingsKey.LOGIN_RESPONSE.name)
                            Throwable(e)
                            null
                        }
                    }
                }
            }

            defaultRequest {
                url {
                    protocol = URLProtocol.HTTPS
                    host = "tasky.pl-coding.com"
                }
                headers {
                    append("x-api-key", API_KEY)
                }
            }
        }
}
