package com.example.tasky.android.alarm.di

import com.example.tasky.alarm.data.AlarmRepository
import com.example.tasky.alarm.domain.IAlarmRepository
import com.example.tasky.android.alarm.data.AlarmScheduler
import com.example.tasky.android.alarm.domain.IAlarmScheduler
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val alarmModule =
    module {
        singleOf<AlarmRepository>(::AlarmRepository).bind<IAlarmRepository>()
        singleOf(::AlarmScheduler).bind<IAlarmScheduler>()
    }
