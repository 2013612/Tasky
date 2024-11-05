package com.example.tasky.android.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasky.android.login.screen.RegisterScreenEvent
import com.example.tasky.android.login.screen.RegisterScreenState
import com.example.tasky.common.model.onError
import com.example.tasky.common.model.onSuccess
import com.example.tasky.common.util.Validator
import com.example.tasky.login.data.model.RegisterBody
import com.example.tasky.login.domain.ILoginRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val loginRepository: ILoginRepository,
) : ViewModel() {
    private val _screenStateFlow = MutableStateFlow(RegisterScreenState())
    val screenStateFlow = _screenStateFlow.asStateFlow()

    private val _isRegisterSuccessFlow = MutableStateFlow(false)
    val isRegisterSuccessFlow = _isRegisterSuccessFlow.asStateFlow()

    fun onEvent(event: RegisterScreenEvent) {
        when (event) {
            is RegisterScreenEvent.OnClickRegister -> register()
            is RegisterScreenEvent.OnNameChange ->
                _screenStateFlow.update {
                    it.copy(nameState = it.nameState.copy(text = event.name, isCheckVisible = Validator.validateName(event.name)))
                }
            is RegisterScreenEvent.OnEmailChange ->
                _screenStateFlow.update {
                    it.copy(emailState = it.emailState.copy(text = event.email, isCheckVisible = Validator.validateEmail(event.email)))
                }
            is RegisterScreenEvent.OnPasswordChange ->
                _screenStateFlow.update {
                    it.copy(
                        passwordState = it.passwordState.copy(text = event.password),
                    )
                }
            is RegisterScreenEvent.OnPasswordVisibilityChange ->
                _screenStateFlow.update {
                    it.copy(passwordState = it.passwordState.copy(isVisible = event.isVisible))
                }
        }
    }

    private fun isInputValid(): Boolean {
        val name = screenStateFlow.value.nameState.text
        val email = screenStateFlow.value.emailState.text
        val password = screenStateFlow.value.passwordState.text

        return Validator.validateName(name) && Validator.validateEmail(email) && Validator.validatePassword(password)
    }

    private fun register() {
        if (!isInputValid()) {
            return
        }

        viewModelScope.launch {
            val body =
                RegisterBody(
                    fullName = screenStateFlow.value.nameState.text,
                    email = screenStateFlow.value.emailState.text,
                    password = screenStateFlow.value.passwordState.text,
                )
            loginRepository
                .register(body)
                .onSuccess {
                    _isRegisterSuccessFlow.update { true }
                }.onError {
                    _screenStateFlow.update {
                        it.copy(
                            nameState = it.nameState.copy(errorText = ""),
                            emailState = it.emailState.copy(errorText = ""),
                            passwordState = it.passwordState.copy(errorText = ""),
                        )
                    }
                }
        }
    }
}
