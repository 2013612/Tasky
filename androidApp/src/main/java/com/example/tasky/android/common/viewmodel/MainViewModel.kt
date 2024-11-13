package com.example.tasky.android.common.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasky.agenda.domain.IAgendaRepository
import com.example.tasky.login.domain.manager.LoginManager
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(
    private val loginManager: LoginManager,
    private val agendaRepository: IAgendaRepository,
) : ViewModel() {
    val isLoginFlow = loginManager.isLoginFlow.stateIn(scope = viewModelScope, started = SharingStarted.Eagerly, initialValue = false)

    init {
        subscribeIsLoginFlow()
    }

    private fun subscribeIsLoginFlow() {
        viewModelScope.launch {
            loginManager.isLoginFlow.collect {
                if (it) {
                    viewModelScope.launch {
                        agendaRepository.syncAgenda()
                    }
                }
            }
        }
    }
}
