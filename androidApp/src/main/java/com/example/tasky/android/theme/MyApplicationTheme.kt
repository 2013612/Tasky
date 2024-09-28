package com.example.tasky.android.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colors =
        if (darkTheme) {
            darkColorScheme(
                primary = Color(0xFFBB86FC),
                secondary = Color(0xFF03DAC5),
                tertiary = Color(0xFF3700B3),
            )
        } else {
            lightColorScheme(
                primary = Black,
                secondary = Color(0xFF03DAC5),
                tertiary = Color(0xFF3700B3),
            )
        }

    val typography =
        MyTypography

    val shapes =
        Shapes(
            extraSmall = RoundedCornerShape(5.dp),
            small = RoundedCornerShape(16.dp),
            medium = RoundedCornerShape(20.dp),
            large = RoundedCornerShape(30.dp),
            extraLarge = RoundedCornerShape(38.dp),
        )

    MaterialTheme(
        colorScheme = colors,
        typography = typography,
        shapes = shapes,
        content = content,
    )
}
