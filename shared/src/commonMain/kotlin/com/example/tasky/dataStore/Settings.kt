package com.example.tasky.dataStore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ExperimentalSettingsImplementation
import com.russhwolf.settings.datastore.DataStoreSettings
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.mapLatest

@OptIn(ExperimentalSettingsImplementation::class, ExperimentalSettingsApi::class)
fun createSettings(dataStore: DataStore<Preferences>): DataStoreSettings = DataStoreSettings(dataStore)

@OptIn(
    ExperimentalCoroutinesApi::class,
    ExperimentalSettingsImplementation::class,
    ExperimentalSettingsApi::class,
)
val isLoginFlow = createSettings(dataStore).getStringOrNullFlow(SettingsKey.LOGIN_RESPONSE.name).mapLatest { !it.isNullOrEmpty() }

enum class SettingsKey {
    LOGIN_RESPONSE,
}
