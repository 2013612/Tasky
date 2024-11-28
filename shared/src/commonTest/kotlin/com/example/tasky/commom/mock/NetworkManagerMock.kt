package com.example.tasky.commom.mock

import com.example.tasky.common.domain.INetworkManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class NetworkManagerMock : INetworkManager {
    override fun isConnected(): Boolean = true

    override fun observeHasConnection(): Flow<Boolean> = flow { emit(true) }
}
