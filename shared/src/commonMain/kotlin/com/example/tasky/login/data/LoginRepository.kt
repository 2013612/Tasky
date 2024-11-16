package com.example.tasky.login.data

import com.example.tasky.login.data.model.LoginBody
import com.example.tasky.login.data.model.RegisterBody
import com.example.tasky.login.domain.ILoginRepository

class LoginRepository(
    private val loginDataSource: LoginDataSource = LoginDataSource(),
) : ILoginRepository {
    override suspend fun login(loginBody: LoginBody) = loginDataSource.login(loginBody)

    override suspend fun logout() = loginDataSource.logout()

    override suspend fun register(registerBody: RegisterBody) = loginDataSource.register(registerBody)
}
