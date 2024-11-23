package com.example.tasky.android.common.data

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.tasky.alarm.domain.ISyncAgendaManager
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class BootReceiver :
    BroadcastReceiver(),
    KoinComponent {
    private val syncAgendaManager: ISyncAgendaManager by inject()

    override fun onReceive(
        context: Context?,
        intent: Intent?,
    ) {
        if (intent?.action == "android.intent.action.BOOT_COMPLETED") {
            syncAgendaManager.startPeriodicSyncAgenda()
        }
    }
}
