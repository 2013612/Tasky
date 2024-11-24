package com.example.tasky.android.common.data

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.tasky.agenda.domain.IAgendaRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class BootReceiver :
    BroadcastReceiver(),
    KoinComponent {
    private val agendaRepository: IAgendaRepository by inject()
    private val applicationScope: CoroutineScope by inject()

    override fun onReceive(
        context: Context?,
        intent: Intent?,
    ) {
        if (intent?.action == "android.intent.action.BOOT_COMPLETED") {
            applicationScope.launch {
                agendaRepository.syncAgenda()
            }
        }
    }
}
