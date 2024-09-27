package com.example.tasky.android.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.tasky.android.R

val fontFamily =
    FontFamily(
        Font(R.font.inter_bold),
        Font(R.font.inter_thin),
        Font(R.font.inter_black),
        Font(R.font.inter_light),
        Font(R.font.inter_medium),
        Font(R.font.inter_regular),
        Font(R.font.inter_semibold),
        Font(R.font.inter_extrabold),
        Font(R.font.inter_extralight),
    )

val MyTypography =
    Typography(
        displayMedium = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.Bold, fontFamily = fontFamily),
        displaySmall = TextStyle(fontSize = 26.sp, fontWeight = FontWeight.Bold, fontFamily = fontFamily),
        headlineMedium = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold, fontFamily = fontFamily),
        headlineSmall = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.SemiBold, fontFamily = fontFamily),
        titleMedium = TextStyle(fontSize = 17.sp, fontWeight = FontWeight.Bold, fontFamily = fontFamily),
        bodyLarge = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold, fontFamily = fontFamily),
        bodyMedium = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.SemiBold, fontFamily = fontFamily),
        bodySmall = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Normal, fontFamily = fontFamily),
        labelLarge = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Medium, fontFamily = fontFamily),
        labelMedium = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Light, fontFamily = fontFamily),
        labelSmall = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.Bold, fontFamily = fontFamily),
    )
