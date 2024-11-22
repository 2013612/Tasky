package com.example.tasky.android.alarm.data

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import com.example.tasky.alarm.domain.IAlarmRepository
import com.example.tasky.alarm.domain.IAlarmScheduler
import com.example.tasky.alarm.domain.model.AgendaAlarm
import com.example.tasky.alarm.domain.model.NotificationData
import com.example.tasky.android.alarm.domain.mapper.toNotificationDataParcelable
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlin.time.DurationUnit

class AlarmScheduler(
    private val context: Context,
    private val alarmRepository: IAlarmRepository,
) : IAlarmScheduler {
    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    override suspend fun schedule(data: NotificationData) {
        val triggerAtMillis =
            data.startTime.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds() -
                data.remindAtType.duration.toLong(DurationUnit.MILLISECONDS)

        if (triggerAtMillis <= Clock.System.now().toEpochMilliseconds()) {
            cancel(data.requestCode)
            return
        }

        alarmRepository.upsertAgendaAlarm(AgendaAlarm(data.agendaId, data.requestCode))

        val intent =
            Intent(context, AlarmReceiver::class.java).apply {
                putExtra("DATA", data.toNotificationDataParcelable())
            }

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            triggerAtMillis,
            PendingIntent.getBroadcast(
                context,
                data.hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
            ),
        )
    }

    override suspend fun cancel(requestCode: Int) {
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                requestCode,
                Intent(context, AlarmReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
            ),
        )

        alarmRepository.deleteAgendaAlarm(requestCode = requestCode)
    }

    override fun schedulePeriodicSyncAgenda() {
        val alarmIntent =
            Intent(context, SyncAgendaReceiver::class.java).let { intent ->
                PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
            }

        alarmManager.setInexactRepeating(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime(),
            AlarmManager.INTERVAL_HALF_HOUR,
            alarmIntent,
        )
    }
}
