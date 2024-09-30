package com.example.tasky.android.login.components

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

@Composable
fun CheckTextField(
    text: String,
    onTextChange: (String) -> Unit,
    placeHolder: String,
    isCheckVisible: Boolean,
    errorText: String?,
    modifier: Modifier = Modifier,
) {
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
            if (isCheckVisible) {
                { Icon(Icons.Filled.Check, null, tint = Color.Green) }
            } else {
                null
            },
        supportingText = { Text(errorText ?: "", style = typography.labelSmall, color = Red) },
        isError = errorText != null,
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun CheckTextFieldPreview() {
    MyApplicationTheme {
        CheckTextField("", {}, "placeholder", false, null)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun CheckTextFieldValidPreview() {
    MyApplicationTheme {
        CheckTextField("userName", {}, "placeholder", true, null)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun CheckTextFieldErrorPreview() {
    MyApplicationTheme {
        CheckTextField("", {}, "placeholder", false, "error Text")
    }
}
