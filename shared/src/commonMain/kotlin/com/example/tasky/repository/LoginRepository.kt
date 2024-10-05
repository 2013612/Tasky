package com.example.tasky.repository

import com.example.tasky.dataSource.LoginDataSource
import com.example.tasky.model.login.LoginBody
import kotlinx.coroutines.flow.flow

class LoginRepository(
    private val loginDataSource: LoginDataSource = LoginDataSource(),
) {
    fun login(loginBody: LoginBody) =
        flow {
            emit(loginDataSource.login(loginBody))
        }
}
