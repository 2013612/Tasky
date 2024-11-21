package com.example.tasky.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.tasky.database.model.AgendaAlarmEntity

@Dao
interface AgendaAlarmDao {
    @Query("SELECT * FROM AgendaAlarmEntity WHERE agendaId = :agendaId")
    suspend fun get(agendaId: String): AgendaAlarmEntity

    @Query("SELECT * FROM AgendaAlarmEntity")
    suspend fun getAll(): List<AgendaAlarmEntity>

    @Query("DELETE FROM AgendaAlarmEntity")
    suspend fun deleteAll()

    @Query("DELETE FROM AgendaAlarmEntity where requestCode = :requestCode")
    suspend fun deleteByRequestCode(requestCode: Int)

    @Upsert
    suspend fun upsert(entity: AgendaAlarmEntity)

    @Delete
    suspend fun delete(entity: AgendaAlarmEntity)
}
