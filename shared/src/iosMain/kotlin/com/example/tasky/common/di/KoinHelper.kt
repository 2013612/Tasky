package com.example.tasky.common.di

import com.example.tasky.auth.domain.IAuthRepository
import com.example.tasky.auth.domain.ISessionManager
import org.koin.core.Koin
import org.koin.core.context.startKoin

object KoinHelper {
    private lateinit var koin: Koin

    fun initKoin() {
        val instance =
            startKoin {
                modules(
                    repositoryModule,
                    dataStoreModule,
                    managerModule,
                )
            }

        koin = instance.koin
    }

    fun getIAuthRepository(): IAuthRepository = koin.get()

    fun getISessionManager(): ISessionManager = koin.get()
}
