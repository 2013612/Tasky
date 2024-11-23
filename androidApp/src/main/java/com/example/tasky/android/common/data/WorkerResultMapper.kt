package com.example.tasky.android.common.data

import androidx.work.ListenableWorker
import com.example.tasky.common.data.model.DataError

fun DataError.toWorkerResult(): ListenableWorker.Result =
    when (this) {
        DataError.Remote.REQUEST_TIMEOUT -> ListenableWorker.Result.retry()
        DataError.Remote.UNAUTHORIZED -> ListenableWorker.Result.retry()
        DataError.Remote.CONFLICT -> ListenableWorker.Result.retry()
        DataError.Remote.TOO_MANY_REQUESTS -> ListenableWorker.Result.retry()
        DataError.Remote.NO_INTERNET -> ListenableWorker.Result.retry()
        DataError.Remote.PAYLOAD_TOO_LARGE -> ListenableWorker.Result.failure()
        DataError.Remote.SERVER_ERROR -> ListenableWorker.Result.retry()
        DataError.Remote.SERIALIZATION -> ListenableWorker.Result.failure()
        DataError.Remote.BAD_REQUEST -> ListenableWorker.Result.retry()
        DataError.Remote.FORBIDDEN -> ListenableWorker.Result.retry()
        DataError.Remote.NOT_FOUND -> ListenableWorker.Result.retry()
        DataError.Remote.UNKNOWN -> ListenableWorker.Result.failure()
    }
