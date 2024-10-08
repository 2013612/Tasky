package com.example.tasky.repository

import com.example.tasky.dataSource.LoginDataSource
import com.example.tasky.model.login.LoginBody
import com.example.tasky.model.login.RegisterBody

class LoginRepository(
    private val loginDataSource: LoginDataSource = LoginDataSource(),
) {
    suspend fun login(loginBody: LoginBody) = loginDataSource.login(loginBody)

    suspend fun register(registerBody: RegisterBody) = loginDataSource.register(registerBody)
}
