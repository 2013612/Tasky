package com.example.tasky.common.data.manager

import com.example.tasky.common.domain.INetworkManager
import dev.tmapps.konnection.Konnection
import kotlinx.coroutines.flow.Flow

class NetworkManager : INetworkManager {
    private val konnection = Konnection.instance

    override fun isConnected(): Boolean = konnection.isConnected()

    override fun observeHasConnection(): Flow<Boolean> = konnection.observeHasConnection()
}
