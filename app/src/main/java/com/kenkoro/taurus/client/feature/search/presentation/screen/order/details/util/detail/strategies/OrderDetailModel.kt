package com.kenkoro.taurus.client.feature.search.presentation.screen.order.details.util.detail.strategies

import com.kenkoro.taurus.client.feature.search.presentation.screen.order.details.util.detail.OrderDetailStrategy
import kotlinx.coroutines.delay

class OrderDetailModel : OrderDetailStrategy {
  override suspend fun fetch(filter: String): List<String> {
    delay(100L)
    return listOf(
      "SH 23",
      "SH 24",
      "86F",
    ).filter { it.contains(filter, ignoreCase = true) }
  }
}