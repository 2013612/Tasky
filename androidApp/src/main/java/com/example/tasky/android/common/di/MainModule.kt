package com.example.tasky.android.common.di

import com.example.tasky.android.common.viewmodel.MainViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val mainModule =
    module {
        viewModelOf(::MainViewModel)
    }
