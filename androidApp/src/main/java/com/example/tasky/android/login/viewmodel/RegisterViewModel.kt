package com.example.tasky.android.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasky.android.login.screen.RegisterScreenEvent
import com.example.tasky.android.login.screen.RegisterScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {
    private val _screenStateFlow = MutableStateFlow(RegisterScreenState())
    val screenStateFlow = _screenStateFlow.asStateFlow()

    fun onEvent(event: RegisterScreenEvent) {
        when (event) {
            is RegisterScreenEvent.OnClickRegister -> register()
            is RegisterScreenEvent.OnNameChange -> _screenStateFlow.update { it.copy(nameState = it.nameState.copy(text = event.name)) }
            is RegisterScreenEvent.OnEmailChange -> _screenStateFlow.update { it.copy(emailState = it.emailState.copy(text = event.email)) }
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

    private fun register() {
        viewModelScope.launch {
            // TODO
        }
    }
}
