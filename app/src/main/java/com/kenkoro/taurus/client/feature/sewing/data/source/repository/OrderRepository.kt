package com.kenkoro.taurus.client.feature.sewing.data.source.repository

import com.kenkoro.taurus.client.feature.sewing.data.source.remote.api.OrderApi
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.request.Order
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.request.UpdateRequest
import com.kenkoro.taurus.client.feature.sewing.data.util.OrderDataType
import io.ktor.client.statement.HttpResponse

interface OrderRepository {
  companion object {
    fun create(orderApi: OrderApi): OrderRepositoryImpl {
      return OrderRepositoryImpl(orderApi)
    }
  }

  suspend fun newOrder(request: Order): HttpResponse

  suspend fun getOrder(orderId: Int): HttpResponse

  suspend fun updateOrderData(
    request: UpdateRequest,
    orderId: Int,
    data: OrderDataType
  ): HttpResponse

  suspend fun deleteOrder(orderId: Int): HttpResponse
}