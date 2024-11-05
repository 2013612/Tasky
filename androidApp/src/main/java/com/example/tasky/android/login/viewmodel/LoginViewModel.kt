package com.example.tasky.android.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasky.android.login.screen.LoginScreenEvent
import com.example.tasky.android.login.screen.LoginScreenState
import com.example.tasky.login.data.model.LoginBody
import com.example.tasky.login.domain.manager.loginManager
import com.example.tasky.login.domain.util.Validator
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
            is LoginScreenEvent.OnEmailChange ->
                _screenStateFlow.update {
                    it.copy(emailState = it.emailState.copy(text = event.email, isCheckVisible = Validator.validateEmail(event.email)))
                }
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

    private fun isInputValid(): Boolean {
        val email = screenStateFlow.value.emailState.text
        val password = screenStateFlow.value.passwordState.text

        return Validator.validateEmail(email) && Validator.validatePassword(password)
    }

    private fun login() {
        if (!isInputValid()) {
            return
        }

        viewModelScope.launch {
            val isLoginSuccess =
                loginManager.logIn(
                    LoginBody(email = _screenStateFlow.value.emailState.text, password = _screenStateFlow.value.passwordState.text),
                )

            if (!isLoginSuccess) {
                _screenStateFlow.update {
                    it.copy(emailState = it.emailState.copy(errorText = ""), passwordState = it.passwordState.copy(errorText = ""))
                }
            }
        }
    }
}
