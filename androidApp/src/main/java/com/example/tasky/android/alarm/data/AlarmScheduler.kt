package com.example.tasky.android.alarm.data

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.tasky.agenda.domain.model.AgendaItem
import com.example.tasky.android.alarm.domain.IAlarmScheduler
import kotlin.time.DurationUnit

class AlarmScheduler(
    private val context: Context,
) : IAlarmScheduler {
    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    override fun schedule(agendaItem: AgendaItem) {
        val intent =
            Intent(context, AlarmReceiver::class.java).apply {
                putExtra(AlarmIntentKey.ID.name, agendaItem.id)
                putExtra(AlarmIntentKey.TITLE.name, agendaItem.title)
                putExtra(AlarmIntentKey.TYPE.name, agendaItem.getAgendaType())
            }

        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            agendaItem.getStartTime() - agendaItem.remindAt.duration.toLong(DurationUnit.MILLISECONDS),
            PendingIntent.getBroadcast(
                context,
                agendaItem.hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
            ),
        )
    }

    override fun cancel(agendaItem: AgendaItem) {
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                agendaItem.hashCode(),
                Intent(context, AlarmReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
            ),
        )
    }
}
