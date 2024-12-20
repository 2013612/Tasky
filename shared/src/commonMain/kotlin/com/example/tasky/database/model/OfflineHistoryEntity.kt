package com.example.tasky.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class OfflineHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val apiType: ApiType,
    val params: String,
    val body: String,
    val userId: String,
)

enum class ApiType {
    DELETE_EVENT,
    DELETE_EVENT_ATTENDEE,
    DELETE_TASK,
    DELETE_REMINDER,
    CREATE_EVENT,
    CREATE_TASK,
    CREATE_REMINDER,
    UPDATE_EVENT,
    UPDATE_TASK,
    UPDATE_REMINDER,
}

fun ApiType.isDelete(): Boolean {
    val validTypes = listOf(ApiType.DELETE_REMINDER, ApiType.DELETE_EVENT, ApiType.DELETE_EVENT_ATTENDEE, ApiType.DELETE_TASK)
    return validTypes.contains(this)
}
