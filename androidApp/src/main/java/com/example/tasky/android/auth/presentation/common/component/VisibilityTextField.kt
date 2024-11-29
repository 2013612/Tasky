package com.example.tasky.android.auth.presentation.common.component

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

data class VisibilityTextFieldState(
    val text: String = "",
    val isVisible: Boolean = false,
    val errorText: String? = null,
)

@Composable
fun VisibilityTextField(
    state: VisibilityTextFieldState,
    placeHolder: String,
    onTextChange: (String) -> Unit,
    onVisibilityChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        state.text,
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
            {
                IconButton(onClick = { onVisibilityChange(!state.isVisible) }) {
                    Icon(
                        painterResource(if (state.isVisible) R.drawable.visibility else R.drawable.visibility_off),
                        null,
                    )
                }
            },
        supportingText = { Text(state.errorText ?: "", style = typography.labelSmall, color = Red) },
        isError = state.errorText != null,
        visualTransformation = if (state.isVisible) VisualTransformation.None else PasswordVisualTransformation(),
    )
}

@Preview
@Composable
private fun VisibilityTextFieldPreview() {
    MyApplicationTheme {
        VisibilityTextField(VisibilityTextFieldState(text = "password"), "password", {}, {})
    }
}

@Preview
@Composable
private fun VisibilityTextFieldShowPreview() {
    MyApplicationTheme {
        VisibilityTextField(VisibilityTextFieldState(text = "password", isVisible = true), "password", {}, {})
    }
}
