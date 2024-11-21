package com.example.tasky.android.alarm.domain

import com.example.tasky.android.alarm.domain.model.NotificationData

interface IAlarmScheduler {
    fun schedule(data: NotificationData)

    fun cancel(requestCode: Int)
}
