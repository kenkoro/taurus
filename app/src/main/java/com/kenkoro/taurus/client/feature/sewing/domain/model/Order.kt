package com.kenkoro.taurus.client.feature.sewing.domain.model

data class Order(
  val id: Int,
  val customer: String,
  val date: String,
  val orderId: Int,
  val title: String,
  val model: String,
  val size: String,
  val color: String,
  val category: String,
  val quantity: Int,
  val status: String,
)