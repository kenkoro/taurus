package com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.response

import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.request.OrderRequestDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetOrdersResponseDto(
  @SerialName("paginated_orders") val paginatedOrders: List<OrderRequestDto>,
  @SerialName("has_next_page") val hasNextPage: Boolean,
)