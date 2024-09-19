package com.kenkoro.taurus.client.feature.orders.presentation.screen.order.util

import com.kenkoro.taurus.client.core.connectivity.NetworkStatus
import com.kenkoro.taurus.client.feature.orders.domain.OrderStatus
import com.kenkoro.taurus.client.feature.shared.states.AuthStatus

data class OrderScreenUtils(
  val authStatus: AuthStatus,
  val network: NetworkStatus,
  val proceedAuth: (AuthStatus) -> Unit = {},
  val saveTheRestDetails: (orderId: Int, date: Long, status: OrderStatus) -> Unit,
)