package com.kenkoro.taurus.client.feature.search.presentation.screen.order.details.util.detail.strategies

import com.kenkoro.taurus.client.feature.search.presentation.screen.order.details.util.detail.OrderDetailStrategy
import kotlinx.coroutines.delay

class OrderDetailSize : OrderDetailStrategy {
  override suspend fun fetch(filter: String): List<String> {
    delay(100L)
    return listOf(
      "S/M",
      "XS/S",
    ).filter { it.contains(filter, ignoreCase = true) }
  }
}