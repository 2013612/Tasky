package com.example.tasky.android.agenda.di

import com.example.tasky.agenda.data.AgendaRepository
import com.example.tasky.agenda.domain.IAgendaRepository
import com.example.tasky.android.agenda.presentation.agenda.screen.AgendaViewModel
import com.example.tasky.auth.data.manager.SessionManager
import com.example.tasky.auth.domain.ISessionManager
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val agendaModule =
    module {
        single<AgendaRepository> {
            AgendaRepository(get(), get(), get())
        }.bind<IAgendaRepository>()
        single<ISessionManager> {
            SessionManager
        }
        viewModelOf(::AgendaViewModel)
    }
