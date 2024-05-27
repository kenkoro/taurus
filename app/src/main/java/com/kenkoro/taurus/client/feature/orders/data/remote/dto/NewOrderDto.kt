package com.kenkoro.taurus.client.feature.orders.data.remote.dto

import com.kenkoro.taurus.client.feature.orders.domain.OrderStatus
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NewOrderDto(
  @SerialName("order_id") val orderId: Int,
  val customer: String,
  val title: String,
  val model: String,
  val size: String,
  val color: String,
  val category: String,
  val quantity: Int,
  val status: OrderStatus,
  @SerialName("creator_id") val creatorId: Int,
)