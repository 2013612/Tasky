package com.example.tasky.android.common.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasky.agenda.domain.IAgendaRepository
import com.example.tasky.android.alarm.data.SyncAgendaManager
import com.example.tasky.android.common.data.BootReceiverManager
import com.example.tasky.auth.domain.ISessionManager
import com.example.tasky.common.domain.INetworkManager
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn

class MainViewModel(
    networkManager: INetworkManager,
    private val agendaRepository: IAgendaRepository,
    bootReceiverManager: BootReceiverManager,
    syncAgendaManager: SyncAgendaManager,
    sessionManager: ISessionManager,
) : ViewModel() {
    val isLoginStateFlow =
        sessionManager.isLoginFlow
            .onEach {
                if (it) {
                    bootReceiverManager.start()
                    syncAgendaManager.startPeriodicSyncAgenda()
                } else {
                    bootReceiverManager.stop()
                    syncAgendaManager.stopPeriodicSyncAgenda()
                }
            }.stateIn(scope = viewModelScope, started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000), initialValue = true)

    init {
        combine(
            networkManager.observeHasConnection(),
            sessionManager.isLoginFlow,
        ) { hasConnection, isLogin ->
            hasConnection && isLogin
        }.filter {
            it
        }.onEach {
            agendaRepository.syncAgenda()
        }.launchIn(viewModelScope)
    }
}
