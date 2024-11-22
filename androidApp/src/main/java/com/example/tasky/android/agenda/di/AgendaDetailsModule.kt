package com.example.tasky.android.agenda.di

import com.example.tasky.android.agenda.viewmodel.AgendaDetailsViewModel
import com.example.tasky.android.utils.IImageCompressor
import com.example.tasky.android.utils.ImageCompressor
import dev.tmapps.konnection.Konnection
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val agendaDetailsModule =
    module {
        factoryOf(::ImageCompressor).bind<IImageCompressor>()
        single<Konnection> {
            Konnection.instance
        }
        viewModelOf(::AgendaDetailsViewModel)
    }
