package com.kenkoro.taurus.client.feature.orders.data.remote.repository

import com.kenkoro.taurus.client.feature.orders.data.remote.api.CutOrderRemoteApi
import com.kenkoro.taurus.client.feature.orders.data.remote.dto.ActualCutOrdersQuantityDto
import com.kenkoro.taurus.client.feature.orders.data.remote.dto.CutOrderDto
import com.kenkoro.taurus.client.feature.orders.data.remote.dto.NewCutOrderDto

interface CutOrderRepository {
  companion object {
    fun create(api: CutOrderRemoteApi): CutOrderRepositoryImpl = CutOrderRepositoryImpl(api)
  }

  suspend fun addNewCutOrder(
    dto: NewCutOrderDto,
    token: String,
  ): Result<CutOrderDto>

  suspend fun getActualCutOrdersQuantity(
    orderId: Int,
    token: String,
  ): Result<ActualCutOrdersQuantityDto>
}