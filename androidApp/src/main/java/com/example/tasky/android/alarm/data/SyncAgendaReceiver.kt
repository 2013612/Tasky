package com.example.tasky.android.alarm.data

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.tasky.agenda.domain.IAgendaRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SyncAgendaReceiver :
    BroadcastReceiver(),
    KoinComponent {
    private val agendaRepository: IAgendaRepository by inject()
    private val applicationScope by inject<CoroutineScope>()

    override fun onReceive(
        context: Context?,
        intent: Intent?,
    ) {
        applicationScope.launch {
            agendaRepository.syncAgenda()
        }
    }
}
