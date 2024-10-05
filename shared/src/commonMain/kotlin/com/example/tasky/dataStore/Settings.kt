package com.example.tasky.dataStore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ExperimentalSettingsImplementation
import com.russhwolf.settings.datastore.DataStoreSettings

@OptIn(ExperimentalSettingsImplementation::class, ExperimentalSettingsApi::class)
fun createSettings(dataStore: DataStore<Preferences>): DataStoreSettings = DataStoreSettings(dataStore)

enum class SettingsKey {
    LOGIN_RESPONSE,
}
