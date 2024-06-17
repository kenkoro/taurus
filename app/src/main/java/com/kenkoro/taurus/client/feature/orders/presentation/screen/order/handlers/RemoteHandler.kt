package com.kenkoro.taurus.client.feature.orders.presentation.screen.order.handlers

import com.kenkoro.taurus.client.feature.orders.data.remote.dto.NewOrderDto
import com.kenkoro.taurus.client.feature.orders.data.remote.dto.OrderDto
import com.kenkoro.taurus.client.feature.orders.domain.NewOrder
import com.kenkoro.taurus.client.feature.profile.data.remote.dto.UserDto
import com.kenkoro.taurus.client.feature.shared.data.remote.dto.TokenDto

data class RemoteHandler(
  val login: suspend (subject: String, password: String) -> Result<TokenDto>,
  val getUser: suspend (subject: String, token: String) -> Result<UserDto>,
  val addNewOrder: suspend (newOrder: NewOrder) -> Result<OrderDto>,
  val deleteOrder: suspend (orderId: Int, deleterSubject: String) -> Boolean = { _, _ -> false },
  val editOrder: suspend (NewOrderDto, Int, String, String) -> Boolean = { _, _, _, _ -> false },
)