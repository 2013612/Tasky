package com.example.tasky.android.agenda.di

import com.example.tasky.android.agenda.viewmodel.AgendaViewModel
import com.example.tasky.repository.AgendaRepository
import com.example.tasky.repository.IAgendaRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val agendaModule =
    module {
        singleOf<AgendaRepository>(::AgendaRepository) {
            bind<IAgendaRepository>()
        }
        viewModelOf(::AgendaViewModel)
    }
