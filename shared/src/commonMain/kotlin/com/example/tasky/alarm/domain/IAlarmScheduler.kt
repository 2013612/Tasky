package com.example.tasky.alarm.domain

import com.example.tasky.alarm.domain.model.NotificationData

interface IAlarmScheduler {
    suspend fun schedule(data: NotificationData)

    suspend fun cancel(requestCode: Int)
}
