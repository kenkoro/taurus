package com.kenkoro.taurus.client.feature.orders.presentation.screen.order.util

import androidx.paging.PagingData
import com.kenkoro.taurus.client.feature.orders.data.local.OrderEntity

interface OrderFilterStrategy {
  fun filter(pagingData: PagingData<OrderEntity>)
}