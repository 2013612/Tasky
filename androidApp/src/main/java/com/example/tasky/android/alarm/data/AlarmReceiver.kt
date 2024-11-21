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
                    .Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.tasky_logo)
                    .setContentTitle(data.title)
                    .setContentText(data.description)
                    .setAutoCancel(true)

            createNotificationChannel()

            if (areNotificationsEnabled()) {
                notify(data.hashCode(), builder.build())
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
