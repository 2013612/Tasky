package com.example.tasky.common.data.manager

import com.example.tasky.BuildKonfig.API_KEY
import com.example.tasky.auth.data.manager.SessionManager
import com.example.tasky.auth.data.mapper.toAccessTokenBody
import com.example.tasky.auth.data.model.AccessTokenResponse
import com.example.tasky.common.data.model.ErrorResponse
import com.example.tasky.common.data.util.isSuccess
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
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import kotlin.coroutines.cancellation.CancellationException

internal object HttpManager {
    private const val TIMEOUT = 60_000L
    val json =
        Json {
            prettyPrint = true
            ignoreUnknownKeys = true
        }

    private val sessionManager = SessionManager

    val httpClient =
        HttpClient {
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
                        sessionManager.loadTokens()
                    }

                    refreshTokens {
                        try {
                            val body = sessionManager.loadAccessTokenBody()?.toAccessTokenBody() ?: throw Exception("no login response")
                            val response =
                                client.post("/accessToken") {
                                    markAsRefreshTokenRequest()
                                    setBody(body)
                                }

                            if (!response.isSuccess()) {
                                val errorResponse = response.body<ErrorResponse>()
                                throw Exception(errorResponse.message)
                            }

                            val responseBody = response.body<AccessTokenResponse>()

                            sessionManager.updateAccessToken(responseBody.accessToken, responseBody.expirationTimestamp)

                            BearerTokens(responseBody.accessToken, body.refreshToken)
                        } catch (e: Exception) {
                            if (e is CancellationException) throw e

                            println("refresh token fail: ${e.message}")

                            sessionManager.removeToken()
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
                header("x-api-key", API_KEY)
                contentType(ContentType.Application.Json)
            }
        }
}
