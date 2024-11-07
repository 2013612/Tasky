package com.example.tasky.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.tasky.agenda.domain.model.RemindAtType

@Entity
data class ReminderEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val time: Long,
    val remindAt: RemindAtType,
)
