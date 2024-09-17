package com.kenkoro.taurus.client.feature.auth.presentation.util

import com.kenkoro.taurus.client.core.connectivity.NetworkStatus
import com.kenkoro.taurus.client.feature.shared.states.AuthStatus

data class AuthUtils(
  val network: NetworkStatus,
  val exit: () -> Unit = {},
  val proceedAuth: (AuthStatus) -> Unit = {},
)