package com.example.tasky

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
