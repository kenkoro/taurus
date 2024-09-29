package com.kenkoro.taurus.client.feature.orders.presentation.screen.order.util

import com.kenkoro.taurus.client.core.connectivity.NetworkStatus
import com.kenkoro.taurus.client.feature.orders.domain.OrderStatus
import com.kenkoro.taurus.client.feature.profile.domain.User
import com.kenkoro.taurus.client.feature.shared.components.util.NavItemWithNavigation
import com.kenkoro.taurus.client.feature.shared.data.remote.dto.TokenDto
import com.kenkoro.taurus.client.feature.shared.states.AuthStatus

data class OrderScreenShared(
  val user: User?,
  val authStatus: AuthStatus,
  val network: NetworkStatus,
  val proceedAuth: (AuthStatus) -> Unit = {},
  val saveTheRestOfDetails: (orderId: Int, date: Long, status: OrderStatus) -> Unit,
  val getUser: suspend (subject: String, token: TokenDto, postAction: () -> Unit) -> Boolean,
  val currentRoute: String,
  val items: List<NavItemWithNavigation>,
)