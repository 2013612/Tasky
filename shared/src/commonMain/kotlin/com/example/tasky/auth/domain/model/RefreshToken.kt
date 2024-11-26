package com.example.tasky.auth.domain.model

data class RefreshToken(
    val refreshToken: String,
    val userId: String,
)
