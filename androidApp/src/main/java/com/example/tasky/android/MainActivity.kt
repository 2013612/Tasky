package com.example.tasky.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.example.tasky.android.common.presentation.viewmodel.MainViewModel
import com.example.tasky.android.theme.MyApplicationTheme
import org.koin.android.ext.android.inject
import org.koin.androidx.compose.KoinAndroidContext

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        setContent {
            MyApplicationTheme {
                KoinAndroidContext {
                    Scaffold { innerPadding ->
                        val navController = rememberNavController()
                        val isLogin = viewModel.isLoginStateFlow.collectAsState().value
                        MainNavHost(navController, isLogin = isLogin, Modifier.padding(innerPadding))
                    }
                }
            }
        }
    }
}
