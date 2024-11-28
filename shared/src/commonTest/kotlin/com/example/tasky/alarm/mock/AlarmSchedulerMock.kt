package com.example.tasky.alarm.mock

import com.example.tasky.alarm.domain.IAlarmScheduler
import com.example.tasky.alarm.domain.model.NotificationData

class AlarmSchedulerMock : IAlarmScheduler {
    val alarms: MutableList<NotificationData> = emptyList<NotificationData>().toMutableList()

    override suspend fun schedule(data: NotificationData) {
        alarms.add(data)
    }

    override suspend fun cancel(requestCode: Int) {
        alarms.removeAll { it.requestCode == requestCode }
    }
}
