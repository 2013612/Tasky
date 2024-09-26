package com.example.tasky.android.login.screen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.tasky.Greeting
import kotlinx.serialization.Serializable

fun NavGraphBuilder.loginScreen() {
    composable<Login> {
        LoginScreen()
    }
}

@Serializable
object Login

@Composable
internal fun LoginScreen(modifier: Modifier = Modifier) {
    Text(Greeting().greet(), modifier)
}
