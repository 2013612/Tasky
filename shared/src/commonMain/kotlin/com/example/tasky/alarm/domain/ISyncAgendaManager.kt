package com.example.tasky.alarm.domain

interface ISyncAgendaManager {
    fun startPeriodicSyncAgenda()

    fun stopPeriodicSyncAgenda()
}
