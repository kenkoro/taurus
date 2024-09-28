package com.kenkoro.taurus.client.feature.search.presentation.screen.order.details.util.detail

interface OrderDetailStrategy {
  suspend fun fetch(filter: String): List<String>
}