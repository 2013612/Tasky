package com.example.tasky.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AgendaAlarmEntity(
    @PrimaryKey val agendaId: String,
    val requestCode: Int,
)
