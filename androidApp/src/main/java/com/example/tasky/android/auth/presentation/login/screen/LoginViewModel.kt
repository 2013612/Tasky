package com.example.tasky.android.auth.presentation.login.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasky.auth.domain.IAuthRepository
import com.example.tasky.auth.domain.util.Validator
import com.example.tasky.common.domain.model.onError
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginRepository: IAuthRepository,
) : ViewModel() {
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
            loginRepository
                .login(
                    email = _screenStateFlow.value.emailState.text,
                    password = _screenStateFlow.value.passwordState.text,
                ).onError {
                    _screenStateFlow.update {
                        it.copy(emailState = it.emailState.copy(errorText = ""), passwordState = it.passwordState.copy(errorText = ""))
                    }
                }
        }
    }
}
