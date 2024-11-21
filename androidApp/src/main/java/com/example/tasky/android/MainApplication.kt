package com.example.tasky.android

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.core.content.getSystemService
import com.example.tasky.android.agenda.di.agendaDetailsModule
import com.example.tasky.android.agenda.di.agendaModule
import com.example.tasky.android.alarm.data.AGENDA_ALARM_CHANNEL_ID
import com.example.tasky.android.alarm.di.alarmModule
import com.example.tasky.android.auth.di.loginModule
import com.example.tasky.android.auth.di.registerModule
import com.example.tasky.android.common.di.dataStoreModule
import com.example.tasky.android.common.di.databaseModule
import com.example.tasky.android.common.di.mainModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(
                mainModule,
                loginModule,
                registerModule,
                agendaModule,
                agendaDetailsModule,
                databaseModule,
                dataStoreModule,
                alarmModule,
            )
        }

        createAgendaAlarmChannel()
    }

    private fun createAgendaAlarmChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.agenda_alarm_channel)
            val descriptionText = getString(R.string.agenda_alarm_channel_desc)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel =
                NotificationChannel(AGENDA_ALARM_CHANNEL_ID, name, importance).apply {
                    description = descriptionText
                }
            getSystemService<NotificationManager>()?.createNotificationChannel(channel)
        }
    }
}
