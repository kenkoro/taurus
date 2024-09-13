package com.kenkoro.taurus.client.feature.orders.domain

import com.kenkoro.taurus.client.feature.orders.data.remote.dto.EditOrderDto

data class EditOrder(
  val orderId: Int,
  val customer: String,
  val date: Long,
  val title: String,
  val model: String,
  val size: String,
  val color: String,
  val category: String,
  val quantity: Int,
  val status: OrderStatus,
  val creatorId: Int,
) {
  fun toEditOrderDto(): EditOrderDto {
    return EditOrderDto(
      orderId = orderId,
      customer = customer,
      title = title,
      model = model,
      size = size,
      color = color,
      category = category,
      quantity = quantity,
      status = status,
      creatorId = creatorId,
    )
  }
}