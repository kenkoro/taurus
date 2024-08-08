package com.kenkoro.taurus.client.feature.orders.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CutOrderDto(
  @SerialName("cut_order_id") val cutOrderId: Int,
  @SerialName("order_id") val orderId: Int,
  val date: Long,
  val quantity: Int,
  @SerialName("cutter_id") val cutterId: Int,
  val comment: String,
)