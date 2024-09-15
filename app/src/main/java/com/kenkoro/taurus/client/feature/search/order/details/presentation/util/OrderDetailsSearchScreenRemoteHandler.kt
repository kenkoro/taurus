package com.kenkoro.taurus.client.feature.search.order.details.presentation.util

data class OrderDetailsSearchScreenRemoteHandler(
  val fetch: suspend (filter: String) -> List<String> = { emptyList() },
)