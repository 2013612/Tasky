package com.example.tasky.manager

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object LoginManager {
    private val _isLoginInFlow = MutableStateFlow(false)
    val isLoginInFlow = _isLoginInFlow.asStateFlow()

    suspend fun logIn(): Boolean {
        delay(1000)
        _isLoginInFlow.update { true }

        return true
    }

    suspend fun logOut() {
        delay(1000)
        _isLoginInFlow.update { false }
    }
}
