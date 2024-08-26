package com.kenkoro.taurus.client.feature.orders.presentation.screen.order.util

import com.kenkoro.taurus.client.feature.orders.data.remote.dto.ActualCutOrdersQuantityDto
import com.kenkoro.taurus.client.feature.orders.data.remote.dto.CutOrderDto
import com.kenkoro.taurus.client.feature.orders.data.remote.dto.OrderDto
import com.kenkoro.taurus.client.feature.orders.domain.NewCutOrder
import com.kenkoro.taurus.client.feature.orders.domain.NewOrder
import com.kenkoro.taurus.client.feature.profile.data.remote.dto.UserDto
import com.kenkoro.taurus.client.feature.shared.data.remote.dto.TokenDto

data class OrderScreenRemoteHandler(
  val login: suspend (subject: String, password: String) -> Result<TokenDto>,
  val getUser: suspend (subject: String, token: String) -> Result<UserDto>,
  val addNewOrder: suspend (newOrder: NewOrder) -> Result<OrderDto>,
  val deleteOrder: suspend (orderId: Int, deleterSubject: String) -> Boolean = { _, _ -> false },
  val editOrder: suspend (NewOrder, String) -> Boolean = { _, _ -> false },
  val addNewCutOrder: suspend (NewCutOrder) -> Result<CutOrderDto>,
  val getActualCutOrdersQuantity: suspend (Int) -> Result<ActualCutOrdersQuantityDto>,
)