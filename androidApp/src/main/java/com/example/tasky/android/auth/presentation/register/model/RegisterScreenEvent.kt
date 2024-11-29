package com.example.tasky.android.auth.presentation.register.model

sealed interface RegisterScreenEvent {
    data object OnClickRegister : RegisterScreenEvent

    data class OnNameChange(
        val name: String,
    ) : RegisterScreenEvent

    data class OnEmailChange(
        val email: String,
    ) : RegisterScreenEvent

    data class OnPasswordChange(
        val password: String,
    ) : RegisterScreenEvent

    data class OnPasswordVisibilityChange(
        val isVisible: Boolean,
    ) : RegisterScreenEvent
}
