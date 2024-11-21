package com.example.tasky.android.alarm.data

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.getSystemService
import com.example.tasky.android.R
import com.example.tasky.android.alarm.domain.model.NotificationDataParcelable
import com.example.tasky.android.utils.getCompatParcelableExtra

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(
        context: Context?,
        intent: Intent?,
    ) {
        val data = intent?.getCompatParcelableExtra<NotificationDataParcelable>("DATA") ?: return

        println("onReceive $data")

        val notificationManager =
            context?.getSystemService<NotificationManager>() ?: return

        with(notificationManager) {
            val builder =
                NotificationCompat
                    .Builder(context, AGENDA_ALARM_CHANNEL_ID)
                    .setSmallIcon(R.drawable.tasky_logo)
                    .setContentTitle(data.title)
                    .setContentText(data.description)
                    .setAutoCancel(true)

            if (areNotificationsEnabled()) {
                notify(data.hashCode(), builder.build())
            }
        }
    }
}

const val AGENDA_ALARM_CHANNEL_ID = "AGENDA_ALARM_CHANNEL_ID"
