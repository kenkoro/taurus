package com.kenkoro.taurus.client.feature.sewing.data.source.mappers

import com.kenkoro.taurus.client.feature.sewing.data.source.local.OrderEntity
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.request.OrderRequestDto
import com.kenkoro.taurus.client.feature.sewing.domain.model.Order

fun OrderRequestDto.toOrderEntity(): OrderEntity {
  return OrderEntity(
    orderId = orderId,
    customer = customer,
    date = date,
    title = title,
    model = model,
    size = size,
    color = color,
    category = category,
    quantity = quantity,
    status = status,
  )
}

fun OrderEntity.toOrder(): Order {
  return Order(
    orderId = orderId,
    customer = customer,
    date = date,
    title = title,
    model = model,
    size = size,
    color = color,
    category = category,
    quantity = quantity,
    status = status,
  )
}

fun Order.toOrderEntity(): OrderEntity {
  return OrderEntity(
    orderId = orderId,
    customer = customer,
    date = date,
    title = title,
    model = model,
    size = size,
    color = color,
    category = category,
    quantity = quantity,
    status = status,
  )
}