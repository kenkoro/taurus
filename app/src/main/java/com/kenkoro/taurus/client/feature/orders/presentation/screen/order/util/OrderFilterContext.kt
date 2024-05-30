package com.kenkoro.taurus.client.feature.orders.presentation.screen.order.util

import androidx.paging.PagingData
import com.kenkoro.taurus.client.feature.orders.data.local.OrderEntity

class OrderFilterContext(private var strategy: OrderFilterStrategy? = null) {
  fun strategy(strategy: OrderFilterStrategy?) {
    this.strategy = strategy
  }

  fun filter(pagingData: PagingData<OrderEntity>) {
    strategy?.filter(pagingData)
  }
}