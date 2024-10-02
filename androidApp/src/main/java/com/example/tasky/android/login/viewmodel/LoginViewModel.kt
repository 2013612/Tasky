package com.example.tasky.android.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasky.android.login.screen.LoginScreenEvent
import com.example.tasky.android.login.screen.LoginScreenState
import com.example.tasky.manager.LoginManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val _screenStateFlow = MutableStateFlow(LoginScreenState())
    val screenStateFlow = _screenStateFlow.asStateFlow()

    fun onEvent(event: LoginScreenEvent) {
        when (event) {
            is LoginScreenEvent.OnClickLogin -> login()
            is LoginScreenEvent.OnClickToSignUp -> {}
            is LoginScreenEvent.OnEmailChange -> _screenStateFlow.update { it.copy(emailState = it.emailState.copy(text = event.email)) }
            is LoginScreenEvent.OnPasswordChange ->
                _screenStateFlow.update {
                    it.copy(
                        passwordState = it.passwordState.copy(text = event.password),
                    )
                }
            is LoginScreenEvent.OnPasswordVisibilityChange ->
                _screenStateFlow.update {
                    it.copy(passwordState = it.passwordState.copy(isVisible = event.isVisible))
                }
        }
    }

    private fun login() {
        viewModelScope.launch {
            val isLoginSuccess = LoginManager.logIn()

            if (!isLoginSuccess) {
                _screenStateFlow.update {
                    it.copy(emailState = it.emailState.copy(errorText = ""), passwordState = it.passwordState.copy(errorText = ""))
                }
            }
        }
    }
}
