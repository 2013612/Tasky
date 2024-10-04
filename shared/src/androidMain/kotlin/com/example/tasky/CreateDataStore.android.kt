package com.example.tasky

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.tasky.dataStore.DATA_STORE_FILE_NAME

fun getDataStore(context: Context): DataStore<Preferences> =
    com.example.tasky.dataStore.getDataStore(
        producePath = { context.filesDir.resolve(DATA_STORE_FILE_NAME).absolutePath },
    )
