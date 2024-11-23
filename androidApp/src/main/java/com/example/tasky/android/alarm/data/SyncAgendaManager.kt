package com.example.tasky.android.alarm.data

import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.tasky.alarm.domain.ISyncAgendaManager
import com.example.tasky.android.common.data.BootReceiver
import java.util.concurrent.TimeUnit
import kotlin.time.Duration.Companion.minutes
import kotlin.time.toJavaDuration

class SyncAgendaManager(
    private val workManager: WorkManager,
    private val context: Context,
) : ISyncAgendaManager {
    private val workRequest =
        PeriodicWorkRequestBuilder<SyncAgendaWorker>(30.minutes.toJavaDuration())
            .addTag(SyncAgendaWorker.TAG)
            .setConstraints(
                Constraints
                    .Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build(),
            ).setBackoffCriteria(
                backoffPolicy = BackoffPolicy.EXPONENTIAL,
                backoffDelay = 2000L,
                timeUnit = TimeUnit.MILLISECONDS,
            ).setInitialDelay(
                duration = 30,
                timeUnit = TimeUnit.MINUTES,
            ).build()

    private val receiver = ComponentName(context, BootReceiver::class.java)

    override fun startPeriodicSyncAgenda() {
        workManager.enqueue(workRequest)

        context.packageManager.setComponentEnabledSetting(
            receiver,
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP,
        )
    }

    override fun stopPeriodicSyncAgenda() {
        workManager.cancelWorkById(workRequest.id)

        context.packageManager.setComponentEnabledSetting(
            receiver,
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
            PackageManager.DONT_KILL_APP,
        )
    }
}
