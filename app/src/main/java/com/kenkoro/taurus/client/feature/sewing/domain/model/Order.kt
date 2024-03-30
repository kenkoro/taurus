package com.kenkoro.taurus.client.feature.sewing.domain.model

import com.kenkoro.taurus.client.feature.sewing.data.util.OrderStatus

data class Order(
  val orderId: Int,
  val customer: String,
  val date: String,
  val title: String,
  val model: String,
  val size: String,
  val color: String,
  val category: String,
  val quantity: Int,
  val status: OrderStatus,
)