package com.kenkoro.taurus.client.feature.search.order.details.presentation.util.detail.strategies

import com.kenkoro.taurus.client.feature.search.order.details.presentation.util.detail.OrderDetailStrategy
import kotlinx.coroutines.delay

class OrderDetailCustomer : OrderDetailStrategy {
  override suspend fun fetch(): List<String> {
    delay(2000L)
    return listOf("Result 1", "Result 2", "Result 3", "Result 4", "Result 5", "Result 6")
  }
}