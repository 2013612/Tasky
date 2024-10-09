package com.example.tasky.repository

import com.example.tasky.dataSource.LoginDataSource
import com.example.tasky.model.BaseError
import com.example.tasky.model.ResultWrapper
import com.example.tasky.model.login.LoginBody
import com.example.tasky.model.login.LoginResponse
import com.example.tasky.model.login.RegisterBody

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
