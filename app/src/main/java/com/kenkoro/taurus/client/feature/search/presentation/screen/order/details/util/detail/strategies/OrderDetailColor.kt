package com.kenkoro.taurus.client.feature.search.presentation.screen.order.details.util.detail.strategies

import com.kenkoro.taurus.client.feature.search.presentation.screen.order.details.util.detail.OrderDetailStrategy
import kotlinx.coroutines.delay

class OrderDetailColor : OrderDetailStrategy {
  override suspend fun fetch(filter: String): List<String> {
    delay(100L)
    return listOf(
      "Black",
      "Silver",
      "Graphite",
    ).filter { it.contains(filter, ignoreCase = true) }
  }
}