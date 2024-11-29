package com.example.tasky.android.auth.presentation.register.model

import com.example.tasky.android.auth.presentation.common.component.CheckTextFieldState
import com.example.tasky.android.auth.presentation.common.component.VisibilityTextFieldState

data class RegisterScreenState(
    val nameState: CheckTextFieldState = CheckTextFieldState(),
    val emailState: CheckTextFieldState = CheckTextFieldState(),
    val passwordState: VisibilityTextFieldState = VisibilityTextFieldState(),
)
