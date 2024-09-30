package com.example.tasky.android.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasky.android.login.screen.LoginScreenEvent
import com.example.tasky.android.login.screen.LoginScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val _screenStateFlow = MutableStateFlow(LoginScreenState())
    val screenStateFlow = _screenStateFlow.asStateFlow()

    private val _isLoginSuccessFlow = MutableStateFlow(false)
    val isLoginSuccessFlow = _isLoginSuccessFlow.asStateFlow()

    fun onEvent(event: LoginScreenEvent) {
        when (event) {
            is LoginScreenEvent.OnClickLogin -> login()
            is LoginScreenEvent.OnClickToSignUp -> {}
            is LoginScreenEvent.OnEmailStateChange -> _screenStateFlow.value = screenStateFlow.value.copy(emailState = event.newState)
            is LoginScreenEvent.OnPasswordStateChange -> _screenStateFlow.value = screenStateFlow.value.copy(passwordState = event.newState)
        }
    }

    private fun login() {
        viewModelScope.launch {
            _isLoginSuccessFlow.value = true
        }
    }
}
