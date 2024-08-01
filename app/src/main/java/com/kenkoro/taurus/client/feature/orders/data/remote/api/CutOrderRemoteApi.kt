package com.kenkoro.taurus.client.feature.orders.data.remote.api

import com.kenkoro.taurus.client.feature.orders.data.remote.dto.ActualCutOrdersQuantityDto
import com.kenkoro.taurus.client.feature.orders.data.remote.dto.CutOrderDto
import com.kenkoro.taurus.client.feature.orders.data.remote.dto.NewCutOrderDto

interface CutOrderRemoteApi {
  suspend fun addNewCutOrder(
    dto: NewCutOrderDto,
    token: String,
  ): CutOrderDto

  suspend fun getActualCutOrdersQuantity(
    orderId: Int,
    token: String,
  ): ActualCutOrdersQuantityDto
}