package com.kenkoro.taurus.client.feature.orders.presentation.screen.order.util

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.kenkoro.taurus.client.feature.orders.domain.Order

class OrderFilterContext {
  private var strategy by mutableStateOf<OrderFilterStrategy?>(null)

  fun strategy(strategy: OrderFilterStrategy?) {
    this.strategy = strategy
  }

  fun filter(order: Order): Boolean = strategy?.filter(order) ?: true
}