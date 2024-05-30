package com.kenkoro.taurus.client.feature.orders.presentation.screen.order.util

import androidx.paging.PagingData
import androidx.paging.map
import com.kenkoro.taurus.client.feature.orders.data.local.OrderEntity
import com.kenkoro.taurus.client.feature.orders.domain.OrderStatus.Cut

class InspectorOrderFilter : OrderFilterStrategy {
  override fun filter(pagingData: PagingData<OrderEntity>) {
    pagingData.map { it.status == Cut }
  }
}