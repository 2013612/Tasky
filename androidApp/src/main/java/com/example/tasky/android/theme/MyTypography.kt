package com.example.tasky.android.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.tasky.android.R

private val Inter =
    FontFamily(
        Font(R.font.inter_bold, weight = FontWeight.Bold),
        Font(R.font.inter_thin, weight = FontWeight.Thin),
        Font(R.font.inter_black, weight = FontWeight.Black),
        Font(R.font.inter_light, weight = FontWeight.Light),
        Font(R.font.inter_medium, weight = FontWeight.Medium),
        Font(R.font.inter_regular, weight = FontWeight.Normal),
        Font(R.font.inter_semibold, weight = FontWeight.SemiBold),
        Font(R.font.inter_extrabold, weight = FontWeight.ExtraBold),
        Font(R.font.inter_extralight, weight = FontWeight.ExtraLight),
    )

val MyTypography =
    Typography(
        displayMedium = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.Bold, fontFamily = Inter),
        displaySmall = TextStyle(fontSize = 26.sp, fontWeight = FontWeight.Bold, fontFamily = Inter),
        headlineMedium = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold, fontFamily = Inter),
        headlineSmall = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.SemiBold, fontFamily = Inter),
        titleMedium = TextStyle(fontSize = 17.sp, fontWeight = FontWeight.Bold, fontFamily = Inter),
        bodyLarge = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold, fontFamily = Inter),
        bodyMedium = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.SemiBold, fontFamily = Inter),
        bodySmall = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Normal, fontFamily = Inter),
        labelLarge = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Medium, fontFamily = Inter),
        labelMedium = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Light, fontFamily = Inter),
        labelSmall = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.Bold, fontFamily = Inter),
    )
