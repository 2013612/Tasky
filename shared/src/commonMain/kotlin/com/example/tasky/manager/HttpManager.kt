package com.example.tasky.manager

import com.example.tasky.BuildKonfig.API_KEY
import com.example.tasky.dataStore.SettingsKey
import com.example.tasky.dataStore.createSettings
import com.example.tasky.dataStore.getDataStore
import com.example.tasky.model.login.LoginResponse
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ExperimentalSettingsImplementation
import io.ktor.client.HttpClient
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
import io.ktor.http.URLProtocol
import io.ktor.http.headers
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

internal object HttpManager {
    private const val TIMEOUT = 60_000L

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
                    Json {
                        prettyPrint = true
                    },
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
                                    Json.decodeFromString<LoginResponse>(
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
