package com.example.tasky.android.alarm.di

import com.example.tasky.alarm.data.AlarmRepository
import com.example.tasky.alarm.domain.IAlarmRepository
import com.example.tasky.alarm.domain.IAlarmScheduler
import com.example.tasky.android.MainApplication
import com.example.tasky.android.alarm.data.AlarmScheduler
import kotlinx.coroutines.CoroutineScope
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val alarmModule =
    module {
        single<CoroutineScope> {
            (androidApplication() as MainApplication).applicationScope
        }
        singleOf<AlarmRepository>(::AlarmRepository).bind<IAlarmRepository>()
        singleOf(::AlarmScheduler).bind<IAlarmScheduler>()
    }
