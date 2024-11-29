package com.example.tasky.android.agenda.di

import com.example.tasky.android.agenda.data.ImageCompressor
import com.example.tasky.android.agenda.domain.IImageCompressor
import com.example.tasky.android.agenda.presentation.details.screen.AgendaDetailsViewModel
import com.example.tasky.common.data.manager.NetworkManager
import com.example.tasky.common.domain.INetworkManager
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val agendaDetailsModule =
    module {
        factoryOf(::ImageCompressor).bind<IImageCompressor>()
        singleOf(::NetworkManager).bind<INetworkManager>()
        viewModelOf(::AgendaDetailsViewModel)
    }
