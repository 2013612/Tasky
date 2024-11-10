package com.example.tasky.android.common.di

import com.example.tasky.database.AppDatabase
import com.example.tasky.database.createDatabase
import com.example.tasky.getDatabaseBuilder
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val databaseModule =
    module(createdAtStart = true) {
        single<AppDatabase> {
            createDatabase(getDatabaseBuilder(androidApplication()))
        }
    }
