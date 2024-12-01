package com.example.tasky.android.alarm.data

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import androidx.core.content.getSystemService
import androidx.core.net.toUri
import com.example.tasky.alarm.domain.IAlarmRepository
import com.example.tasky.android.AGENDA_DETAILS_DEEPLINK
import com.example.tasky.android.MainActivity
import com.example.tasky.android.R
import com.example.tasky.android.alarm.domain.model.NotificationDataParcelable
import com.example.tasky.android.common.presentation.utils.getCompatParcelableExtra
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AlarmReceiver :
    BroadcastReceiver(),
    KoinComponent {
    private val alarmRepository: IAlarmRepository by inject()
    private val applicationScope by inject<CoroutineScope>()

    override fun onReceive(
        context: Context?,
        intent: Intent?,
    ) {
        val data = intent?.getCompatParcelableExtra<NotificationDataParcelable>("DATA") ?: return

        println("onReceive $data")

        val notificationManager =
            context?.getSystemService<NotificationManager>() ?: return

        with(notificationManager) {
            val activityIntent =
                Intent(context, MainActivity::class.java).apply {
                    this.data = "$AGENDA_DETAILS_DEEPLINK/${data.agendaId}/${data.type}/${false}".toUri()
                }
            val pendingIntent =
                TaskStackBuilder.create(context).run {
                    addNextIntentWithParentStack(activityIntent)
                    getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE)
                }

            val builder =
                NotificationCompat
                    .Builder(context, AGENDA_ALARM_CHANNEL_ID)
                    .setSmallIcon(R.drawable.notification)
                    .setColor(context.getColor(R.color.app_logo_green))
                    .setContentTitle(data.title)
                    .setContentText(data.description)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)

            if (areNotificationsEnabled()) {
                notify(data.hashCode(), builder.build())
            }
        }

        val pendingResult = goAsync()
        applicationScope.launch {
            alarmRepository.deleteAgendaAlarm(agendaId = data.agendaId)
            pendingResult.finish()
        }
    }
}

const val AGENDA_ALARM_CHANNEL_ID = "AGENDA_ALARM_CHANNEL_ID"
