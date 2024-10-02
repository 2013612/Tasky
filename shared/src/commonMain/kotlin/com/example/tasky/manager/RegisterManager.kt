package com.example.tasky.manager

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object RegisterManager {
    private val _isRegisterSuccessFlow = MutableStateFlow(false)
    val isRegisterSuccessFlow = _isRegisterSuccessFlow.asStateFlow()

    suspend fun register(): Boolean {
        delay(1000)
        _isRegisterSuccessFlow.update { true }

        return true
    }
}
