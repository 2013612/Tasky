package com.example.tasky.android.common.di

import com.example.tasky.android.common.data.BootReceiverManager
import com.example.tasky.android.common.domain.IBootReceiverManager
import com.example.tasky.android.common.viewmodel.MainViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val mainModule =
    module {
        singleOf(::BootReceiverManager).bind<IBootReceiverManager>()
        viewModelOf(::MainViewModel)
    }
