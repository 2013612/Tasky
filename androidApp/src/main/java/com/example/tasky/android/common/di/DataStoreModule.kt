package com.example.tasky.android.common.di

import com.example.tasky.dataStore.createSettings
import com.example.tasky.getDataStore
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ExperimentalSettingsImplementation
import com.russhwolf.settings.datastore.DataStoreSettings
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

@OptIn(ExperimentalSettingsImplementation::class, ExperimentalSettingsApi::class)
val dataStoreModule =
    module {
        single<DataStoreSettings> {
            createSettings(getDataStore(androidApplication()))
        }
    }
