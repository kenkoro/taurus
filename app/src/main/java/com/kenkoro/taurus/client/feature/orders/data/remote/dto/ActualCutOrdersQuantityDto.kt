package com.kenkoro.taurus.client.feature.orders.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ActualCutOrdersQuantityDto(
  @SerialName("actual_quantity") val actualQuantity: Int,
)