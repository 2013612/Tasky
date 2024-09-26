package com.example.tasky.android

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.tasky.android.login.LoginGraph
import com.example.tasky.android.login.loginGraph

@Composable
fun MainNavHost(
    navHostController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(navController = navHostController, startDestination = LoginGraph, modifier = modifier) {
        loginGraph(navHostController)
    }
}
