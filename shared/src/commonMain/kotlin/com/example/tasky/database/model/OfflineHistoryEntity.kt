package com.example.tasky.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class OfflineHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val apiType: ApiType,
    val body: String,
    val userId: String,
)

enum class ApiType {
    DELETE_EVENT,
    DELETE_TASK,
    DELETE_REMINDER,
}
