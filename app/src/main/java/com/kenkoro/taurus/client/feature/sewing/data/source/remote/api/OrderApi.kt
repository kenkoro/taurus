package com.kenkoro.taurus.client.feature.sewing.data.source.remote.api

import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.request.Order
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.request.UpdateRequest
import com.kenkoro.taurus.client.feature.sewing.data.util.OrderDataType
import io.ktor.client.statement.HttpResponse

interface OrderApi {
  companion object {
    var token: String = ""

    fun token(token: String) {
      UserApi.token = token
    }
  }

  suspend fun newOrder(request: Order): HttpResponse

  suspend fun getOrder(orderId: Int): HttpResponse

  suspend fun getOrders(page: Int, perPage: Int): HttpResponse

  suspend fun updateOrderData(
    request: UpdateRequest,
    orderId: Int,
    data: OrderDataType
  ): HttpResponse

  suspend fun deleteOrder(orderId: Int): HttpResponse
}