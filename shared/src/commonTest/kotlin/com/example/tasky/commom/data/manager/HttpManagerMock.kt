package com.example.tasky.commom.data.manager

import com.example.tasky.BuildKonfig.API_KEY
import com.example.tasky.auth.data.model.AccessTokenResponse
import com.example.tasky.auth.domain.manager.SessionManagerMock
import com.example.tasky.common.data.model.ErrorResponse
import com.example.tasky.common.data.util.isSuccess
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import kotlin.coroutines.cancellation.CancellationException

class HttpManagerMock(
    mockEngine: MockEngine,
) {
    private val json =
        Json {
            prettyPrint = true
            ignoreUnknownKeys = true
        }

    val httpClient =
        HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(
                    json,
                )
            }

            install(Auth) {
                bearer {
                    loadTokens {
                        SessionManagerMock.loadTokens()
                    }

                    refreshTokens {
                        try {
                            val body = SessionManagerMock.loadAccessTokenBody() ?: throw Exception("no login response")
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

                            SessionManagerMock.updateAccessToken(responseBody.accessToken, responseBody.expirationTimestamp)

                            BearerTokens(responseBody.accessToken, body.refreshToken)
                        } catch (e: Exception) {
                            if (e is CancellationException) throw e

                            println("refresh token fail: ${e.message}")

                            SessionManagerMock.removeToken()
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
