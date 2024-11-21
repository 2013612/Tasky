package com.example.tasky.android.alarm.data

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.tasky.alarm.domain.IAlarmRepository
import com.example.tasky.alarm.domain.model.AgendaAlarm
import com.example.tasky.android.alarm.domain.IAlarmScheduler
import com.example.tasky.android.alarm.domain.mapper.toNotificationDataParcelable
import com.example.tasky.android.alarm.domain.model.NotificationData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlin.time.DurationUnit

class AlarmScheduler(
    private val context: Context,
    private val alarmRepository: IAlarmRepository,
) : IAlarmScheduler {
    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    override fun schedule(data: NotificationData) {
        CoroutineScope(Dispatchers.IO).launch {
            alarmRepository.upsertAgendaAlarm(AgendaAlarm(data.agendaId, data.requestCode))
        }

        val intent =
            Intent(context, AlarmReceiver::class.java).apply {
                putExtra("DATA", data.toNotificationDataParcelable())
            }

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            data.startTime.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds() -
                data.remindAtType.duration.toLong(DurationUnit.MILLISECONDS),
            PendingIntent.getBroadcast(
                context,
                data.hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
            ),
        )
    }

    override fun cancel(requestCode: Int) {
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                requestCode,
                Intent(context, AlarmReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
            ),
        )

        CoroutineScope(Dispatchers.IO).launch {
            alarmRepository.deleteAgendaAlarm(requestCode = requestCode)
        }
    }
}
