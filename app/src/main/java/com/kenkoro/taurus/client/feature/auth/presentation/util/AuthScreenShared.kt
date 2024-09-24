package com.kenkoro.taurus.client.feature.auth.presentation.util

import com.kenkoro.taurus.client.core.connectivity.NetworkStatus
import com.kenkoro.taurus.client.feature.shared.data.remote.dto.TokenDto
import com.kenkoro.taurus.client.feature.shared.states.AuthStatus

data class AuthScreenShared(
  val network: NetworkStatus,
  val exit: () -> Unit = {},
  val proceedAuth: (AuthStatus) -> Unit = {},
  val getUser: suspend (subject: String, token: TokenDto, postAction: () -> Unit) -> Boolean,
)