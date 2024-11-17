package com.example.tasky.android.common.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasky.agenda.domain.IAgendaRepository
import com.example.tasky.dataStore.isLoginFlow
import dev.tmapps.konnection.Konnection
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn

class MainViewModel(
    konnection: Konnection,
    private val agendaRepository: IAgendaRepository,
) : ViewModel() {
    val isLoginStateFlow = isLoginFlow.stateIn(scope = viewModelScope, started = SharingStarted.Eagerly, initialValue = false)

    init {
        combine(
            konnection.observeHasConnection(),
            isLoginFlow,
        ) { hasConnection, isLogin ->
            hasConnection && isLogin
        }.filter {
            it
        }.onEach {
            agendaRepository.syncAgenda()
        }.launchIn(viewModelScope)
    }
}
