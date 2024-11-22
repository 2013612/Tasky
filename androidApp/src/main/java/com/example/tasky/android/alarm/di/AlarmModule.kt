package com.example.tasky.android.alarm.di

import com.example.tasky.alarm.data.AlarmRepository
import com.example.tasky.alarm.domain.IAlarmRepository
import com.example.tasky.alarm.domain.IAlarmScheduler
import com.example.tasky.android.alarm.data.AlarmScheduler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val alarmModule =
    module {
        factory { CoroutineScope(Dispatchers.IO) }
        singleOf<AlarmRepository>(::AlarmRepository).bind<IAlarmRepository>()
        singleOf(::AlarmScheduler).bind<IAlarmScheduler>()
    }
