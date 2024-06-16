package com.kenkoro.taurus.client.feature.orders.presentation.screen.order.util

import com.kenkoro.taurus.client.feature.orders.domain.Order
import com.kenkoro.taurus.client.feature.orders.domain.OrderStatus.Checked

class ManagerOrderFilter : OrderFilterStrategy {
  override fun filter(order: Order): Boolean = order.status == Checked
}