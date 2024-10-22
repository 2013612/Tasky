package com.example.tasky.dataSource

import com.example.tasky.manager.HttpManager
import com.example.tasky.model.BaseError
import com.example.tasky.model.ResultWrapper
import com.example.tasky.model.login.Login
import com.example.tasky.model.login.LoginBody
import com.example.tasky.model.login.LoginResponse
import com.example.tasky.model.login.Register
import com.example.tasky.model.login.RegisterBody
import com.example.tasky.util.safeCall
import io.ktor.client.HttpClient
import io.ktor.client.plugins.resources.post
import io.ktor.client.request.setBody

class LoginDataSource(
    private val httpClient: HttpClient = HttpManager.httpClient,
) {
    suspend fun login(loginBody: LoginBody): ResultWrapper<LoginResponse, BaseError> =
        safeCall {
            httpClient.post(Login()) {
                setBody(loginBody)
            }
        }

    suspend fun register(registerBody: RegisterBody): ResultWrapper<Unit, BaseError> =
        safeCall {
            httpClient.post(Register()) {
                setBody(registerBody)
            }
        }
}
