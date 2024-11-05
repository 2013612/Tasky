package com.example.tasky.android

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.tasky.android.agenda.AgendaGraph
import com.example.tasky.android.agenda.agendaGraph
import com.example.tasky.android.login.LoginGraph
import com.example.tasky.android.login.loginGraph
import com.example.tasky.login.domain.manager.loginManager

@Composable
fun MainNavHost(
    navHostController: NavHostController,
    modifier: Modifier = Modifier,
) {
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
