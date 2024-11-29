package com.example.tasky.android.auth.presentation.login.model

import com.example.tasky.android.auth.presentation.common.component.CheckTextFieldState
import com.example.tasky.android.auth.presentation.common.component.VisibilityTextFieldState

data class LoginScreenState(
    val emailState: CheckTextFieldState = CheckTextFieldState(),
    val passwordState: VisibilityTextFieldState = VisibilityTextFieldState(),
)
