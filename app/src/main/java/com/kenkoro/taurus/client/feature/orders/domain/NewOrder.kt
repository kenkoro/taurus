package com.kenkoro.taurus.client.feature.orders.domain

data class NewOrder(
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
)