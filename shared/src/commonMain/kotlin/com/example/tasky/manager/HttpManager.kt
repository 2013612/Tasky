package com.example.tasky.manager

import com.example.tasky.BuildKonfig.API_KEY
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
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
