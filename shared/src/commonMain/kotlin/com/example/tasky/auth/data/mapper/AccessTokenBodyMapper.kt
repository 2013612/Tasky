package com.example.tasky.auth.data.mapper

import com.example.tasky.auth.data.model.AccessTokenBody
import com.example.tasky.auth.domain.model.RefreshToken

fun RefreshToken.toAccessTokenBody() = AccessTokenBody(refreshToken, userId)
