package com.kenkoro.taurus.client.feature.orders.presentation.screen.order.util

import com.kenkoro.taurus.client.feature.orders.domain.EditOrder
import com.kenkoro.taurus.client.feature.orders.domain.NewOrder
import com.kenkoro.taurus.client.feature.orders.domain.Order
import com.kenkoro.taurus.client.feature.profile.domain.User

data class OrderScreenLocalHandler(
  val addNewUser: suspend (User) -> Unit = {},
  val addNewOrder: suspend (NewOrder, Int) -> Unit = { _, _ -> },
  val deleteOrder: suspend (Order) -> Unit = {},
  val editOrder: suspend (EditOrder, Int) -> Unit = { _, _ -> },
)