package com.example.tasky.android.login.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tasky.android.login.components.CheckTextField
import com.example.tasky.android.login.components.CheckTextFieldState
import com.example.tasky.android.login.components.VisibilityTextField
import com.example.tasky.android.login.components.VisibilityTextFieldState
import com.example.tasky.android.theme.Black
import com.example.tasky.android.theme.MyApplicationTheme

data class RegisterScreenState(
    val nameState: CheckTextFieldState = CheckTextFieldState(),
    val emailState: CheckTextFieldState = CheckTextFieldState(),
    val passwordState: VisibilityTextFieldState = VisibilityTextFieldState(),
)

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

@Composable
private fun RegisterScreen(
    state: RegisterScreenState,
    onEvent: (RegisterScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier.fillMaxSize().background(Black),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(47.dp))
        Text(
            "Create your account",
            style = typography.displayMedium,
            lineHeight = 30.sp,
            color = Color.White,
        )
        Spacer(Modifier.height(40.dp))
        Column(
            Modifier
                .fillMaxSize()
                .background(
                    Color.White,
                    RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
                ).padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(50.dp))
            CheckTextField(
                state = state.nameState,
                onTextChange = {
                    onEvent(
                        RegisterScreenEvent.OnNameChange(it),
                    )
                },
                placeHolder = "Name",
                modifier = Modifier.fillMaxWidth(),
            )
            CheckTextField(
                state = state.emailState,
                onTextChange = {
                    onEvent(
                        RegisterScreenEvent.OnEmailChange(it),
                    )
                },
                placeHolder = "Email address",
                modifier = Modifier.fillMaxWidth(),
            )
            VisibilityTextField(
                state = state.passwordState,
                onTextChange = {
                    onEvent(
                        RegisterScreenEvent.OnPasswordChange(it),
                    )
                },
                placeHolder = "Password",
                onVisibilityChange = {
                    onEvent(
                        RegisterScreenEvent.OnPasswordVisibilityChange(it),
                    )
                },
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.height(25.dp))
            Button(onClick = {
                onEvent(RegisterScreenEvent.OnClickRegister)
            }, modifier = Modifier.fillMaxWidth().height(55.dp)) {
                Text("GET STARTED")
            }
            Spacer(Modifier.weight(1f))
        }
    }
}

@Preview
@Composable
private fun RegisterScreenPreview() {
    MyApplicationTheme {
        RegisterScreen(
            state = RegisterScreenState(),
            onEvent = {},
        )
    }
}