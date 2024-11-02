package com.example.tasky.android.agenda.di

import com.example.tasky.android.agenda.viewmodel.AgendaDetailsViewModel
import com.example.tasky.android.utils.IImageCompressor
import com.example.tasky.android.utils.ImageCompressor
import com.example.tasky.repository.AgendaRepository
import com.example.tasky.repository.IAgendaRepository
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val agendaDetailsModule =
    module {
        singleOf<AgendaRepository>(::AgendaRepository).bind<IAgendaRepository>()
        factoryOf(::ImageCompressor).bind<IImageCompressor>()
        viewModelOf(::AgendaDetailsViewModel)
    }
