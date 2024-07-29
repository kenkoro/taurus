package com.kenkoro.taurus.client.feature.orders.data.mappers

import com.kenkoro.taurus.client.feature.orders.data.local.OrderEntity
import com.kenkoro.taurus.client.feature.orders.data.remote.dto.NewOrderDto
import com.kenkoro.taurus.client.feature.orders.data.remote.dto.OrderDto
import com.kenkoro.taurus.client.feature.orders.domain.NewOrder
import com.kenkoro.taurus.client.feature.orders.domain.Order
import com.kenkoro.taurus.client.feature.orders.domain.OrderStatus

fun OrderDto.toOrderEntity(): OrderEntity =
  OrderEntity(
    recordId = recordId,
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
    creatorId = creatorId,
  )

fun Order.toOrderDto(): OrderDto =
  OrderDto(
    recordId = recordId,
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
    creatorId = creatorId,
  )

fun OrderEntity.toOrder(): Order =
  Order(
    recordId = recordId,
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
    creatorId = creatorId,
  )

fun Order.toOrderEntity(): OrderEntity =
  OrderEntity(
    recordId = recordId,
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
    creatorId = creatorId,
  )

fun NewOrder.toOrderEntity(orderId: Int): OrderEntity =
  OrderEntity(
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
    creatorId = creatorId,
  )

fun NewOrder.toNewOrderDto(): NewOrderDto =
  NewOrderDto(
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

fun Order.toNewOrder(): NewOrder =
  NewOrder(
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
    creatorId = creatorId,
  )

fun Order.toCutOrder(): NewOrder =
  NewOrder(
    orderId = orderId,
    customer = customer,
    date = date,
    title = title,
    model = model,
    size = size,
    color = color,
    category = category,
    quantity = quantity,
    status = OrderStatus.Cut,
    creatorId = creatorId,
  )

fun Order.toCheckedOrder(): NewOrder =
  NewOrder(
    orderId = orderId,
    customer = customer,
    date = date,
    title = title,
    model = model,
    size = size,
    color = color,
    category = category,
    quantity = quantity,
    status = OrderStatus.Checked,
    creatorId = creatorId,
  )