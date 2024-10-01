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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withLink
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.tasky.android.login.components.CheckTextField
import com.example.tasky.android.login.components.CheckTextFieldState
import com.example.tasky.android.login.components.VisibilityTextField
import com.example.tasky.android.login.components.VisibilityTextFieldState
import com.example.tasky.android.login.viewmodel.LoginViewModel
import com.example.tasky.android.theme.Black
import com.example.tasky.android.theme.Gray
import com.example.tasky.android.theme.LightBlue
import com.example.tasky.android.theme.MyApplicationTheme
import kotlinx.serialization.Serializable

fun NavGraphBuilder.loginScreen(
    navigateToAgenda: () -> Unit,
    navigateToSignUp: () -> Unit,
) {
    composable<Login> {
        val viewModel: LoginViewModel = viewModel()

        val loginState by viewModel.screenStateFlow.collectAsStateWithLifecycle()
        val isLoginSuccess by viewModel.isLoginSuccessFlow.collectAsStateWithLifecycle()

        LaunchedEffect(isLoginSuccess) {
            if (isLoginSuccess) {
                navigateToAgenda()
            }
        }

        LoginScreen(
            state = loginState,
            onEvent = { event ->
                when (event) {
                    is LoginScreenEvent.OnClickToSignUp -> navigateToSignUp()
                    else -> viewModel.onEvent(event)
                }
            },
        )
    }
}

@Serializable
object Login

data class LoginScreenState(
    val emailState: CheckTextFieldState = CheckTextFieldState(),
    val passwordState: VisibilityTextFieldState = VisibilityTextFieldState(),
)

sealed interface LoginScreenEvent {
    data object OnClickToSignUp : LoginScreenEvent

    data object OnClickLogin : LoginScreenEvent

    data class OnEmailStateChange(val newState: CheckTextFieldState) : LoginScreenEvent

    data class OnPasswordStateChange(val newState: VisibilityTextFieldState) : LoginScreenEvent
}

@Composable
private fun LoginScreen(
    state: LoginScreenState,
    onEvent: (LoginScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val annotatedString =
        buildAnnotatedString {
            append("DON'T HAVE AN ACCOUNT? ")
            withLink(
                LinkAnnotation.Clickable(
                    tag = "tag",
                    styles =
                        TextLinkStyles(
                            style = SpanStyle(color = LightBlue),
                        ),
                    linkInteractionListener = {
                        onEvent(LoginScreenEvent.OnClickToSignUp)
                    },
                ),
            ) {
                append("SIGN UP")
            }
        }
    Column(
        modifier.fillMaxSize().background(Black),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(47.dp))
        Text(
            "Welcome Back!",
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
                state = state.emailState,
                onTextChange = {
                    onEvent(
                        LoginScreenEvent.OnEmailStateChange(
                            state.emailState.copy(
                                text = it,
                            ),
                        ),
                    )
                },
                placeHolder = "Email address",
                modifier = Modifier.fillMaxWidth(),
            )
            VisibilityTextField(
                state = state.passwordState,
                onTextChange = {
                    onEvent(
                        LoginScreenEvent.OnPasswordStateChange(
                            state.passwordState.copy(
                                text = it,
                            ),
                        ),
                    )
                },
                placeHolder = "Password",
                onVisibilityChange = {
                    onEvent(
                        LoginScreenEvent.OnPasswordStateChange(
                            state.passwordState.copy(
                                isVisible = it,
                            ),
                        ),
                    )
                },
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.height(25.dp))
            Button(onClick = {
                onEvent(LoginScreenEvent.OnClickLogin)
            }, modifier = Modifier.fillMaxWidth().height(55.dp)) {
                Text("LOG IN")
            }
            Spacer(Modifier.weight(1f))
            Text(annotatedString, style = typography.labelLarge, lineHeight = 30.sp, color = Gray)
            Spacer(Modifier.height(20.dp))
        }
    }
}

@Preview
@Composable
private fun LoginScreenPreview() {
    MyApplicationTheme {
        LoginScreen(
            state = LoginScreenState(),
            onEvent = {},
        )
    }
}
