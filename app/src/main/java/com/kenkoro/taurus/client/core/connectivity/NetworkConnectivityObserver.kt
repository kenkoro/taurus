package com.kenkoro.taurus.client.core.connectivity

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class NetworkConnectivityObserver(
  context: Context
) : ConnectivityObserver {
  private val connectivityManager =
    context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

  override fun observer(): Flow<Status> {
    return callbackFlow {
      val callback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
          super.onAvailable(network)
          launch { send(Status.Available) }
        }

        override fun onLosing(network: Network, maxMsToLive: Int) {
          super.onLosing(network, maxMsToLive)
          launch { send(Status.Losing) }
        }

        override fun onLost(network: Network) {
          super.onLost(network)
          launch { send(Status.Lost) }
        }

        override fun onUnavailable() {
          super.onUnavailable()
          launch { send(Status.Unavailable) }
        }
      }

      connectivityManager.registerDefaultNetworkCallback(callback)
      awaitClose {
        connectivityManager.unregisterNetworkCallback(callback)
      }
    }.distinctUntilChanged()
  }
}