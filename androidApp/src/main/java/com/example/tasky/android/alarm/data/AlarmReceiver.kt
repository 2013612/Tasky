package com.example.tasky.android.alarm.data

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.getSystemService
import com.example.tasky.agenda.domain.model.AgendaType
import com.example.tasky.android.R

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(
        context: Context?,
        intent: Intent?,
    ) {
        val id = intent?.getStringExtra(AlarmIntentKey.ID.name) ?: return
        val title = intent.getStringExtra(AlarmIntentKey.TITLE.name) ?: return
        val desc = intent.getStringExtra(AlarmIntentKey.DESC.name) ?: return
        val type =
            if (Build.VERSION.SDK_INT >= 33) {
                intent.getSerializableExtra(AlarmIntentKey.TYPE.name, AgendaType::class.java)
            } else {
                intent.getSerializableExtra(AlarmIntentKey.TYPE.name) ?: return
            }

        println("onReceive $id $title $type")

        val notificationManager =
            context?.getSystemService<NotificationManager>() ?: return

        with(notificationManager) {
            val builder =
                NotificationCompat
                    .Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.tasky_logo)
                    .setContentTitle(title)
                    .setContentText(desc)
                    .setAutoCancel(true)

            createNotificationChannel()

            if (areNotificationsEnabled()) {
                notify(id.hashCode(), builder.build())
            }
        }
    }
}

private const val CHANNEL_ID = "CHANNEL_ID"

private fun NotificationManager.createNotificationChannel() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = "NotificationChannel"
        val descriptionText = "NotificationDescription"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel =
            NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
        createNotificationChannel(channel)
    }
}
