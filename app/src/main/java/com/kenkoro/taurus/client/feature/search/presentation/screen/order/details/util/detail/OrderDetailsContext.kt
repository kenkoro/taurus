package com.kenkoro.taurus.client.feature.search.presentation.screen.order.details.util.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class OrderDetailsContext {
  private var strategy by mutableStateOf<OrderDetailStrategy?>(null)

  fun strategy(strategy: OrderDetailStrategy?) {
    this.strategy = strategy
  }

  suspend fun fetch(filter: String): List<String> = strategy?.fetch(filter) ?: emptyList()
}