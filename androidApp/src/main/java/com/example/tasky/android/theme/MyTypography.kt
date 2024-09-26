package com.example.tasky.android.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import com.example.tasky.android.R

val provider =
    GoogleFont.Provider(
        providerAuthority = "com.google.android.gms.fonts",
        providerPackage = "com.google.android.gms",
        certificates = R.array.com_google_android_gms_fonts_certs,
    )

val fontName = GoogleFont("Inter")

val fontFamily = FontFamily(Font(googleFont = fontName, fontProvider = provider))

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
