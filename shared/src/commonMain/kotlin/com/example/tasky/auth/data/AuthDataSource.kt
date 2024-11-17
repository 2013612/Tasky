package com.example.tasky.auth.data

import com.example.tasky.auth.data.model.LoginBody
import com.example.tasky.auth.data.model.LoginResponse
import com.example.tasky.auth.data.model.RegisterBody
import com.example.tasky.common.data.manager.HttpManager
import com.example.tasky.common.data.model.DataError
import com.example.tasky.common.data.util.invalidateBearerTokens
import com.example.tasky.common.data.util.safeCall
import com.example.tasky.common.domain.model.ResultWrapper
import com.example.tasky.common.domain.model.onSuccess
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class AuthDataSource(
    private val httpClient: HttpClient = HttpManager.httpClient,
) {
    suspend fun login(loginBody: LoginBody): ResultWrapper<LoginResponse, DataError.Remote> =
        safeCall<LoginResponse> {
            httpClient.post("/login") {
                setBody(loginBody)
            }
        }.onSuccess {
            httpClient.invalidateBearerTokens()
        }

    suspend fun logout(): ResultWrapper<Unit, DataError.Remote> =
        safeCall<Unit> {
            httpClient.get("/logout")
        }.onSuccess {
            httpClient.invalidateBearerTokens()
        }

    suspend fun register(registerBody: RegisterBody): ResultWrapper<Unit, DataError.Remote> =
        safeCall {
            httpClient.post("/register") {
                setBody(registerBody)
            }
        }
}
