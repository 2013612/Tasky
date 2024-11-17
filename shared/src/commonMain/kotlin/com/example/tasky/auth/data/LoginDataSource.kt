package com.example.tasky.auth.data

import com.example.tasky.auth.data.model.LoginBody
import com.example.tasky.auth.data.model.LoginResponse
import com.example.tasky.auth.data.model.RegisterBody
import com.example.tasky.common.data.manager.HttpManager
import com.example.tasky.common.data.model.BaseError
import com.example.tasky.common.data.util.invalidateBearerTokens
import com.example.tasky.common.data.util.safeCall
import com.example.tasky.common.domain.model.ResultWrapper
import com.example.tasky.common.domain.model.onSuccess
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class LoginDataSource(
    private val httpClient: HttpClient = HttpManager.httpClient,
) {
    suspend fun login(loginBody: LoginBody): ResultWrapper<LoginResponse, BaseError> =
        safeCall<LoginResponse> {
            httpClient.post("/login") {
                setBody(loginBody)
            }
        }.onSuccess {
            httpClient.invalidateBearerTokens()
        }

    suspend fun logout(): ResultWrapper<Unit, BaseError> =
        safeCall<Unit> {
            httpClient.get("/logout")
        }.onSuccess {
            httpClient.invalidateBearerTokens()
        }

    suspend fun register(registerBody: RegisterBody): ResultWrapper<Unit, BaseError> =
        safeCall {
            httpClient.post("/register") {
                setBody(registerBody)
            }
        }
}
