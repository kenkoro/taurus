package com.kenkoro.taurus.client.feature.sewing.data.source.repository

import com.kenkoro.taurus.client.feature.sewing.data.source.remote.api.OrderApi
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.request.Order
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.request.UpdateRequest
import com.kenkoro.taurus.client.feature.sewing.data.util.OrderDataType
import io.ktor.client.statement.HttpResponse

class OrderRepositoryImpl(
  private val orderApi: OrderApi
) : OrderRepository {
  fun token(token: String): OrderRepository {
    OrderApi.token(token)
    return this
  }

  override suspend fun newOrder(request: Order): HttpResponse {
    return orderApi.newOrder(request)
  }

  override suspend fun getOrder(orderId: Int): HttpResponse {
    return orderApi.getOrder(orderId)
  }

  override suspend fun updateOrderData(
    request: UpdateRequest,
    orderId: Int,
    data: OrderDataType
  ): HttpResponse {
    return orderApi.updateOrderData(request, orderId, data)
  }

  override suspend fun deleteOrder(orderId: Int): HttpResponse {
    return orderApi.deleteOrder(orderId)
  }
}