package com.example.tasky.common.di

import com.example.tasky.auth.data.manager.SessionManager
import com.example.tasky.auth.domain.ISessionManager
import org.koin.dsl.bind
import org.koin.dsl.module

val managerModule =
    module {
        single<SessionManager> {
            SessionManager
        }.bind<ISessionManager>()
    }
