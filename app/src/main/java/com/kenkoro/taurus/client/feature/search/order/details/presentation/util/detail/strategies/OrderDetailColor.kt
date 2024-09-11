package com.kenkoro.taurus.client.feature.search.order.details.presentation.util.detail.strategies

import com.kenkoro.taurus.client.feature.search.order.details.presentation.util.detail.OrderDetailStrategy
import kotlinx.coroutines.delay

class OrderDetailColor : OrderDetailStrategy {
  override suspend fun fetch(): List<String> {
    delay(100L)
    return listOf(
      "Black",
      "Silver",
      "Graphite",
    )
  }
}