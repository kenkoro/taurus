package com.kenkoro.taurus.client.feature.orders.presentation.screen.order.util.filter

import com.kenkoro.taurus.client.feature.orders.domain.Order

interface OrderFilterStrategy {
  fun filter(order: Order): Boolean
}