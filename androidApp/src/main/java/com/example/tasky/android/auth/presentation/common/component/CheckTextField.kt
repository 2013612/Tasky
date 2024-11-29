package com.example.tasky.android.auth.presentation.common.component

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tasky.android.theme.Light2
import com.example.tasky.android.theme.LightBlue
import com.example.tasky.android.theme.MyApplicationTheme
import com.example.tasky.android.theme.Red

data class CheckTextFieldState(
    val text: String = "",
    val isCheckVisible: Boolean = false,
    val errorText: String? = null,
)

@Composable
fun CheckTextField(
    state: CheckTextFieldState,
    onTextChange: (String) -> Unit,
    placeHolder: String,
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
            if (state.isCheckVisible) {
                { Icon(Icons.Filled.Check, null, tint = Color.Green) }
            } else {
                null
            },
        supportingText = { Text(state.errorText ?: "", style = typography.labelSmall, color = Red) },
        isError = state.errorText != null,
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun CheckTextFieldPreview() {
    MyApplicationTheme {
        CheckTextField(CheckTextFieldState(), {}, "placeholder")
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun CheckTextFieldValidPreview() {
    MyApplicationTheme {
        CheckTextField(CheckTextFieldState(text = "userName", isCheckVisible = true), {}, "placeholder")
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun CheckTextFieldErrorPreview() {
    MyApplicationTheme {
        CheckTextField(CheckTextFieldState(errorText = "error Text"), {}, "placeholder")
    }
}
