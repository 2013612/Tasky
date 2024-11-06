package com.example.tasky.login.domain

import com.example.tasky.common.data.model.BaseError
import com.example.tasky.common.domain.model.ResultWrapper
import com.example.tasky.login.data.LoginDataSource
import com.example.tasky.login.data.model.LoginBody
import com.example.tasky.login.data.model.LoginResponse
import com.example.tasky.login.data.model.RegisterBody

interface ILoginRepository {
    suspend fun login(loginBody: LoginBody): ResultWrapper<LoginResponse, BaseError>

    suspend fun register(registerBody: RegisterBody): ResultWrapper<Unit, BaseError>
}

class LoginRepository(
    private val loginDataSource: LoginDataSource = LoginDataSource(),
) : ILoginRepository {
    override suspend fun login(loginBody: LoginBody) = loginDataSource.login(loginBody)

    override suspend fun register(registerBody: RegisterBody) = loginDataSource.register(registerBody)
}
