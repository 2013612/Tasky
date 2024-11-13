package com.example.tasky.android.common.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasky.login.domain.manager.LoginManager
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class MainViewModel(
    loginManager: LoginManager,
) : ViewModel() {
    val isLoginFlow = loginManager.isLoginFlow.stateIn(scope = viewModelScope, started = SharingStarted.Eagerly, initialValue = false)
}
