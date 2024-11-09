package com.example.tasky.database.converter

import androidx.room.TypeConverter
import com.example.tasky.database.model.AttendeeSerialized
import com.example.tasky.database.model.PhotoSerialized
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class ListConverter {
    @TypeConverter
    fun attendeeListToString(value: List<AttendeeSerialized>): String = Json.encodeToString(value)

    @TypeConverter
    fun stringToAttendeeList(string: String): List<AttendeeSerialized> = Json.decodeFromString(string)

    @TypeConverter
    fun photoListToString(value: List<PhotoSerialized>): String = Json.encodeToString(value)

    @TypeConverter
    fun stringToPhotoList(string: String): List<PhotoSerialized> = Json.decodeFromString(string)
}
