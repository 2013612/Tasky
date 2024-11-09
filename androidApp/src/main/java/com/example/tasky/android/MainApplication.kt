package com.example.tasky.android

import android.app.Application
import com.example.tasky.android.agenda.di.agendaDetailsModule
import com.example.tasky.android.agenda.di.agendaModule
import com.example.tasky.android.common.di.dataStoreModule
import com.example.tasky.android.common.di.databaseModule
import com.example.tasky.android.common.di.managerModule
import com.example.tasky.android.login.di.loginModule
import com.example.tasky.android.login.di.registerModule
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
                loginModule,
                registerModule,
                agendaModule,
                agendaDetailsModule,
                databaseModule,
                dataStoreModule,
                managerModule,
            )
        }
    }
}
