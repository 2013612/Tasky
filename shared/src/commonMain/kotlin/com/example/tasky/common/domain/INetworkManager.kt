package com.example.tasky.common.domain

import kotlinx.coroutines.flow.Flow

interface INetworkManager {
    fun isConnected(): Boolean

    fun observeHasConnection(): Flow<Boolean>
}
