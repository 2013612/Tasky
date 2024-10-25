package com.example.tasky.android

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.tasky.android.agenda.AgendaGraph
import com.example.tasky.android.agenda.agendaGraph
import com.example.tasky.android.agenda.navigateToAgendaGraph
import com.example.tasky.android.login.LoginGraph
import com.example.tasky.android.login.loginGraph
import com.example.tasky.android.login.navigateToLoginGraph
import com.example.tasky.manager.loginManager

@Composable
fun MainNavHost(
    navHostController: NavHostController,
    modifier: Modifier = Modifier,
) {
    LaunchedEffect(true) {
        loginManager.isLoginFlow.collect { isLogin ->
            if (isLogin) {
                navHostController.navigateToAgendaGraph()
            } else {
                navHostController.navigateToLoginGraph()
            }
        }
    }

    val isLogin = loginManager.isLoginFlow.collectAsState(false).value

    NavHost(
        navController = navHostController,
        startDestination =
            if (isLogin) {
                AgendaGraph
            } else {
                LoginGraph
            },
        modifier = modifier,
    ) {
        loginGraph(navController = navHostController)
        agendaGraph(navController = navHostController)
    }
}
