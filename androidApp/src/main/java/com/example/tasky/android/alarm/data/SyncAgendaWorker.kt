package com.example.tasky.android.alarm.data
import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.tasky.agenda.domain.IAgendaRepository
import com.example.tasky.android.common.data.toWorkerResult
import com.example.tasky.common.domain.model.ResultWrapper

class SyncAgendaWorker(
    context: Context,
    params: WorkerParameters,
    private val agendaRepository: IAgendaRepository,
) : CoroutineWorker(
        context,
        params,
    ) {
    override suspend fun doWork(): Result {
        if (runAttemptCount >= 5) {
            return Result.failure()
        }

        return when (val result = agendaRepository.syncAgenda()) {
            is ResultWrapper.Success -> Result.success()
            is ResultWrapper.Error -> result.error.toWorkerResult()
        }
    }

    companion object {
        const val TAG = "sync_agenda_worker"
    }
}
