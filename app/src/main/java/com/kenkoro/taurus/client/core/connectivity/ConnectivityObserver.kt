package com.kenkoro.taurus.client.core.connectivity

import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {
  fun observer(): Flow<NetworkStatus>
}