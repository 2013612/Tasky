package com.example.tasky.repository

import com.example.tasky.dataSource.AgendaDataSource
import com.example.tasky.model.BaseError
import com.example.tasky.model.ResultWrapper
import com.example.tasky.model.agenda.GetAgendaResponse

interface IAgendaRepository {
    suspend fun getAgenda(timeStamp: Long): ResultWrapper<GetAgendaResponse, BaseError>
}

class AgendaRepository(
    private val agendaDataSource: AgendaDataSource = AgendaDataSource(),
    private val token: String,
) : IAgendaRepository {
    override suspend fun getAgenda(timeStamp: Long) = agendaDataSource.getAgenda(timeStamp = timeStamp, token = token)
}
