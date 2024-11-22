package com.example.tasky.android.common.viewmodel

import android.app.Application
import android.content.ComponentName
import android.content.pm.PackageManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasky.agenda.domain.IAgendaRepository
import com.example.tasky.android.common.data.BootReceiver
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
    application: Application,
) : AndroidViewModel(application) {
    val isLoginStateFlow =
        isLoginFlow
            .onEach {
                if (it) {
                    val receiver = ComponentName(application, BootReceiver::class.java)

                    application.packageManager.setComponentEnabledSetting(
                        receiver,
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                        PackageManager.DONT_KILL_APP,
                    )
                } else {
                    val receiver = ComponentName(application, BootReceiver::class.java)

                    application.packageManager.setComponentEnabledSetting(
                        receiver,
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP,
                    )
                }
            }.stateIn(scope = viewModelScope, started = SharingStarted.Eagerly, initialValue = false)

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
