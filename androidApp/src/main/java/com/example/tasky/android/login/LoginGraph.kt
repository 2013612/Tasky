package com.example.tasky.android.login

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.example.tasky.android.login.screen.Login
import com.example.tasky.android.login.screen.loginScreen
import com.example.tasky.android.login.screen.navigateToRegister
import com.example.tasky.android.login.screen.registerScreen
import kotlinx.serialization.Serializable

@Serializable
object LoginGraph

fun NavGraphBuilder.loginGraph(navController: NavController) {
    navigation<LoginGraph>(startDestination = Login) {
        loginScreen(
            navigateToSignUp = navController::navigateToRegister,
        )
        registerScreen()
    }
}
