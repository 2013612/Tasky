package com.example.tasky.android.agenda.di

import com.example.tasky.agenda.data.AgendaRepository
import com.example.tasky.agenda.domain.IAgendaRepository
import com.example.tasky.android.agenda.viewmodel.AgendaViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val agendaModule =
    module {
        single<AgendaRepository> {
            AgendaRepository(get(), get())
        }.bind<IAgendaRepository>()
        viewModelOf(::AgendaViewModel)
    }
