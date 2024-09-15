package com.kenkoro.taurus.client.feature.search.order.details.presentation.util.detail.strategies

import com.kenkoro.taurus.client.feature.search.order.details.presentation.util.detail.OrderDetailStrategy
import kotlinx.coroutines.delay

class OrderDetailCategory : OrderDetailStrategy {
  override suspend fun fetch(filter: String): List<String> {
    delay(100L)
    return listOf(
      "Пуховики",
      "Костюмы",
    ).filter { it.contains(filter, ignoreCase = true) }
  }
}