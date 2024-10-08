package com.example.tasky.repository

import com.example.tasky.dataSource.LoginDataSource
import com.example.tasky.model.login.LoginBody

class LoginRepository(
    private val loginDataSource: LoginDataSource = LoginDataSource(),
) {
    suspend fun login(loginBody: LoginBody) = loginDataSource.login(loginBody)
}
