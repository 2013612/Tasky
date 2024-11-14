package com.example.tasky.android.common.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasky.agenda.domain.IAgendaRepository
import com.example.tasky.login.domain.manager.LoginManager
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(
    loginManager: LoginManager,
    private val agendaRepository: IAgendaRepository,
) : ViewModel() {
    val isLoginFlow =
        loginManager.isLoginFlow
            .onEach { isLoggedIn ->
                if (isLoggedIn) {
                    viewModelScope.launch {
                        agendaRepository.syncAgenda()
                    }
                }
            }.stateIn(scope = viewModelScope, started = SharingStarted.Eagerly, initialValue = false)
}
