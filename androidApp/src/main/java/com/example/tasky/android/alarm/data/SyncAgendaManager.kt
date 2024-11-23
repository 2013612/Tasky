package com.example.tasky.android.alarm.data

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.tasky.alarm.domain.ISyncAgendaManager
import java.util.concurrent.TimeUnit
import kotlin.time.Duration.Companion.minutes
import kotlin.time.toJavaDuration

class SyncAgendaManager(
    context: Context,
) : ISyncAgendaManager {
    private val workManager = WorkManager.getInstance(context)

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

    override fun startPeriodicSyncAgenda() {
        workManager.enqueue(workRequest)
    }

    override fun stopPeriodicSyncAgenda() {
        workManager.cancelWorkById(workRequest.id)
    }
}
