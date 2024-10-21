package com.example.tasky.repository

import com.example.tasky.dataSource.AgendaDataSource
import com.example.tasky.model.BaseError
import com.example.tasky.model.ResultWrapper
import com.example.tasky.model.agenda.AgendaItem
import com.example.tasky.model.agenda.Event
import com.example.tasky.model.agenda.GetAgendaResponse
import com.example.tasky.model.agenda.Reminder
import com.example.tasky.model.agenda.Task

interface IAgendaRepository {
    suspend fun getAgenda(timeStamp: Long): ResultWrapper<GetAgendaResponse, BaseError>

    suspend fun deleteAgenda(agendaItem: AgendaItem): ResultWrapper<Unit, BaseError>
}

class AgendaRepository(
    private val agendaDataSource: AgendaDataSource = AgendaDataSource(),
) : IAgendaRepository {
    override suspend fun getAgenda(timeStamp: Long) = agendaDataSource.getAgenda(timeStamp = timeStamp)

    override suspend fun deleteAgenda(agendaItem: AgendaItem): ResultWrapper<Unit, BaseError> =
        when (agendaItem) {
            is Event -> agendaDataSource.deleteEvent(agendaItem.id)
            is Task -> agendaDataSource.deleteTask(agendaItem.id)
            is Reminder -> agendaDataSource.deleteTask(agendaItem.id)
        }
}
