package com.kenkoro.taurus.client.feature.search.order.details.presentation.util.detail

interface OrderDetailStrategy {
  suspend fun fetch(): List<String>
}