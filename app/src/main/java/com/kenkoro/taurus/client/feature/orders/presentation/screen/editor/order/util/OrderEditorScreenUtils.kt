package com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.util

import com.kenkoro.taurus.client.core.connectivity.NetworkStatus
import com.kenkoro.taurus.client.feature.orders.domain.OrderStatus
import com.kenkoro.taurus.client.feature.profile.domain.User
import com.kenkoro.taurus.client.feature.shared.states.TaurusTextFieldState

data class OrderEditorScreenUtils(
  val user: User?,
  val orderStatus: OrderStatus,
  val network: NetworkStatus,
  val editOrder: Boolean,
  val changeOrderDetailsSearchScreenBehavior: (TaurusTextFieldState) -> Unit = {},
)