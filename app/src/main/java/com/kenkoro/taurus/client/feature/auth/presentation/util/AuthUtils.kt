package com.kenkoro.taurus.client.feature.auth.presentation.util

import com.kenkoro.taurus.client.core.connectivity.NetworkStatus

data class AuthUtils(
  val network: NetworkStatus,
  val exit: () -> Unit = {},
)