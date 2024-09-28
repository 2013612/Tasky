package com.example.tasky.android.login.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tasky.android.R
import com.example.tasky.android.theme.Light2
import com.example.tasky.android.theme.LightBlue
import com.example.tasky.android.theme.MyApplicationTheme
import com.example.tasky.android.theme.Red

@Composable
fun LoginTextField(
    text: String,
    onTextChange: (String) -> Unit,
    placeHolder: String,
    validator: (String) -> Boolean,
    errorText: String?,
    isPassword: Boolean,
    modifier: Modifier = Modifier,
) {
    var showPassword by rememberSaveable {
        mutableStateOf(false)
    }

    OutlinedTextField(
        text,
        onTextChange,
        placeholder = {
            Text(
                placeHolder,
                style = typography.bodySmall,
                fontWeight = FontWeight.Light,
                lineHeight = 30.sp,
            )
        },
        textStyle = typography.bodySmall.copy(lineHeight = 30.sp),
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        colors =
            OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Light2,
                focusedContainerColor = Light2,
                errorContainerColor = Light2,
                focusedBorderColor = LightBlue,
                errorBorderColor = Red,
                unfocusedBorderColor = Color.Transparent,
            ),
        trailingIcon =
            if (isPassword) {
                if (showPassword) {
                    {
                        Icon(
                            painterResource(R.drawable.visibility),
                            null,
                            modifier =
                                Modifier.clickable {
                                    showPassword = false
                                },
                        )
                    }
                } else {
                    {
                        Icon(
                            painterResource(R.drawable.visibility_off),
                            null,
                            modifier =
                                Modifier.clickable {
                                    showPassword = true
                                },
                        )
                    }
                }
            } else {
                if (validator(text)) {
                    { Icon(Icons.Filled.Check, null, tint = Color.Green) }
                } else {
                    null
                }
            },
        supportingText = { Text(errorText ?: "", style = typography.labelSmall, color = Red) },
        isError = errorText != null,
        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun LoginTextFieldPreview() {
    MyApplicationTheme {
        LoginTextField("", {}, "placeholder", { false }, null, isPassword = false)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun LoginTextFieldValidPreview() {
    MyApplicationTheme {
        LoginTextField("userName", {}, "placeholder", { true }, null, isPassword = false)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun LoginTextFieldErrorPreview() {
    MyApplicationTheme {
        LoginTextField("", {}, "placeholder", { false }, "error Text", isPassword = false)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun LoginTextFieldPasswordPreview() {
    MyApplicationTheme {
        LoginTextField("password", {}, "placeholder", { true }, null, isPassword = true)
    }
}
