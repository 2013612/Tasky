package com.example.tasky.common.di

import com.example.tasky.dataStore.createDataStore
import com.example.tasky.dataStore.createSettings
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ExperimentalSettingsImplementation
import com.russhwolf.settings.coroutines.FlowSettings
import org.koin.dsl.module

@OptIn(ExperimentalSettingsImplementation::class, ExperimentalSettingsApi::class)
val dataStoreModule =
    module(createdAtStart = true) {
        single<FlowSettings> {
            createSettings(createDataStore())
        }
    }
