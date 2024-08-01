package com.kenkoro.taurus.client.feature.orders.domain

data class CutOrder(
  val cutOrderId: Int,
  val orderId: Int,
  val date: Long,
  val quantity: Int,
  val cutterId: Int,
  val comment: String,
)