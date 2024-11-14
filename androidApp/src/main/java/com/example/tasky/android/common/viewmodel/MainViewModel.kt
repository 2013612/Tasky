package com.example.tasky.android.common.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasky.agenda.domain.IAgendaRepository
import com.example.tasky.login.domain.manager.LoginManager
import dev.tmapps.konnection.Konnection
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(
    loginManager: LoginManager,
    konnection: Konnection,
    private val agendaRepository: IAgendaRepository,
) : ViewModel() {
    val isLoginFlow =
        loginManager.isLoginFlow.stateIn(scope = viewModelScope, started = SharingStarted.Eagerly, initialValue = false)

    init {
        viewModelScope.launch {
            konnection
                .observeHasConnection()
                .combine(loginManager.isLoginFlow) { hasConnection, isLogin ->
                    isLogin && hasConnection
                }.collect {
                    if (it) {
                        viewModelScope.launch {
                            agendaRepository.syncAgenda()
                        }
                    }
                }
        }
    }
}
