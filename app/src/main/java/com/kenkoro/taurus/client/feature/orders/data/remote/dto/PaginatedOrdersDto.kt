package com.kenkoro.taurus.client.feature.orders.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PaginatedOrdersDto(
  @SerialName("paginated_orders") val paginatedOrders: List<OrderDto>,
  @SerialName("has_next_page") val hasNextPage: Boolean,
)