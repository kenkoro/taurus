package com.kenkoro.taurus.client.feature.orders.data.remote.api

import com.kenkoro.taurus.client.feature.orders.data.remote.dto.NewOrderDto
import com.kenkoro.taurus.client.feature.orders.data.remote.dto.OrderDto
import com.kenkoro.taurus.client.feature.orders.data.remote.dto.PaginatedOrdersDto
import com.kenkoro.taurus.client.feature.shared.data.remote.dto.DeleteDto
import io.ktor.http.HttpStatusCode

interface OrderRemoteApi {
  suspend fun addNewOrder(
    dto: NewOrderDto,
    token: String,
  ): OrderDto

  suspend fun getOrder(
    orderId: Int,
    token: String,
  ): OrderDto

  suspend fun getPaginatedOrders(
    page: Int,
    perPage: Int,
    token: String,
  ): PaginatedOrdersDto

  suspend fun editOrder(
    dto: NewOrderDto,
    editorSubject: String,
    token: String,
  ): HttpStatusCode

  suspend fun deleteOrder(
    dto: DeleteDto,
    orderId: Int,
    token: String,
  ): HttpStatusCode
}