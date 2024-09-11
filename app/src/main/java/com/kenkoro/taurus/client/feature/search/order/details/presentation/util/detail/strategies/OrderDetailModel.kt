package com.kenkoro.taurus.client.feature.search.order.details.presentation.util.detail.strategies

import com.kenkoro.taurus.client.feature.search.order.details.presentation.util.detail.OrderDetailStrategy
import kotlinx.coroutines.delay

class OrderDetailModel : OrderDetailStrategy {
  override suspend fun fetch(): List<String> {
    delay(100L)
    return listOf(
      "SH 23",
      "SH 24",
      "86F",
    )
  }
}