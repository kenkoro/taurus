package com.kenkoro.taurus.client.feature.sewing.data.source.mappers

import com.kenkoro.taurus.client.feature.sewing.data.source.local.OrderEntity
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.NewOrderDto
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.OrderDto
import com.kenkoro.taurus.client.feature.sewing.domain.model.NewOrder
import com.kenkoro.taurus.client.feature.sewing.domain.model.Order
import com.kenkoro.taurus.client.feature.sewing.domain.model.enums.OrderStatus

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

fun NewOrder.toOrderEntity(): OrderEntity =
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