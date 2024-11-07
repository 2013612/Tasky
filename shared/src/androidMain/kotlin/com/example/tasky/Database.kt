package com.example.tasky

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.tasky.database.AppDatabase
import com.example.tasky.database.DB_FILE_NAME

fun getDatabaseBuilder(ctx: Context): RoomDatabase.Builder<AppDatabase> {
    val appContext = ctx.applicationContext
    val dbFile = appContext.getDatabasePath(DB_FILE_NAME)
    return Room.databaseBuilder<AppDatabase>(
        context = appContext,
        name = dbFile.absolutePath,
    )
}
