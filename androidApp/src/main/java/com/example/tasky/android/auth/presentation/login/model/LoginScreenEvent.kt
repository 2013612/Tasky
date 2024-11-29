package com.example.tasky.android.auth.presentation.login.model

sealed interface LoginScreenEvent {
    data object OnClickToSignUp : LoginScreenEvent

    data object OnClickLogin : LoginScreenEvent

    data class OnEmailChange(val email: String) : LoginScreenEvent

    data class OnPasswordChange(val password: String) : LoginScreenEvent

    data class OnPasswordVisibilityChange(val isVisible: Boolean) : LoginScreenEvent
}
