package com.kenkoro.taurus.client.feature.orders.data.remote.repository

import com.kenkoro.taurus.client.feature.orders.data.remote.api.CutOrderRemoteApi
import com.kenkoro.taurus.client.feature.orders.data.remote.dto.ActualCutOrdersQuantityDto
import com.kenkoro.taurus.client.feature.orders.data.remote.dto.CutOrderDto
import com.kenkoro.taurus.client.feature.orders.data.remote.dto.NewCutOrderDto

class CutOrderRepositoryImpl(
  private val api: CutOrderRemoteApi,
) : CutOrderRepository {
  override suspend fun addNewCutOrder(
    dto: NewCutOrderDto,
    token: String,
  ): Result<CutOrderDto> =
    runCatching {
      api.addNewCutOrder(dto, token)
    }

  override suspend fun getActualCutOrdersQuantity(
    orderId: Int,
    token: String,
  ): Result<ActualCutOrdersQuantityDto> =
    runCatching {
      api.getActualCutOrdersQuantity(orderId, token)
    }
}