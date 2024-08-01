package com.kenkoro.taurus.client.feature.orders.domain

data class NewCutOrder(
  val orderId: Int,
  val date: Long,
  val quantity: Int,
  val cutterId: Int,
  val comment: String,
)