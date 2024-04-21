package com.kenkoro.taurus.client.feature.sewing.domain.model

import com.kenkoro.taurus.client.feature.sewing.data.util.OrderStatus

data class Order(
  val orderId: Int,
  val customer: String = "Заказчик",
  val date: String = "31.03.2024",
  val title: String = "Наименование",
  val model: String = "Модель",
  val size: String = "Размер",
  val color: String = "Цвет",
  val category: String = "Категория",
  val quantity: Int = 0,
  val status: OrderStatus = OrderStatus.Cut,
)