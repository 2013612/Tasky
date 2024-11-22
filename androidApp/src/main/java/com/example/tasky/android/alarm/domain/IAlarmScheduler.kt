package com.example.tasky.android.alarm.domain

import com.example.tasky.android.alarm.domain.model.NotificationData

interface IAlarmScheduler {
    suspend fun schedule(data: NotificationData)

    suspend fun cancel(requestCode: Int)
}
