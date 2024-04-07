package com.kenkoro.taurus.client.feature.sewing.data.source.repository

import com.kenkoro.taurus.client.feature.sewing.data.source.remote.api.OrderApi
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.request.DeleteRequestDto
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.request.OrderRequestDto
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.request.UpdateRequestDto
import com.kenkoro.taurus.client.feature.sewing.data.util.OrderDataType
import io.ktor.client.statement.HttpResponse

interface OrderRepository {
  companion object {
    fun create(orderApi: OrderApi): OrderRepositoryImpl {
      return OrderRepositoryImpl(orderApi)
    }
  }

  suspend fun newOrder(request: OrderRequestDto): HttpResponse

  suspend fun getOrder(orderId: Int): HttpResponse

  suspend fun getOrders(
    page: Int,
    perPage: Int,
  ): HttpResponse

  suspend fun updateOrderData(
    request: UpdateRequestDto,
    orderId: Int,
    data: OrderDataType,
  ): HttpResponse

  suspend fun deleteOrder(
    orderId: Int,
    request: DeleteRequestDto,
  ): HttpResponse
}