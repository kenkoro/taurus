package com.kenkoro.taurus.client.feature.orders.data.remote.repository

import com.kenkoro.taurus.client.feature.orders.data.remote.api.OrderRemoteApi
import com.kenkoro.taurus.client.feature.orders.data.remote.dto.EditOrderDto
import com.kenkoro.taurus.client.feature.orders.data.remote.dto.NewOrderDto
import com.kenkoro.taurus.client.feature.orders.data.remote.dto.OrderDto
import com.kenkoro.taurus.client.feature.orders.data.remote.dto.PaginatedOrdersDto
import com.kenkoro.taurus.client.feature.shared.data.remote.dto.DeleteDto
import io.ktor.http.HttpStatusCode

interface OrderRepository {
  companion object {
    fun create(api: OrderRemoteApi): OrderRepositoryImpl = OrderRepositoryImpl(api)
  }

  suspend fun addNewOrder(
    dto: NewOrderDto,
    token: String,
  ): Result<OrderDto>

  suspend fun getOrder(
    orderId: Int,
    token: String,
  ): Result<OrderDto>

  suspend fun getPaginatedOrders(
    page: Int,
    perPage: Int,
    token: String,
  ): PaginatedOrdersDto

  suspend fun editOrder(
    dto: EditOrderDto,
    editorSubject: String,
    token: String,
  ): Result<HttpStatusCode>

  suspend fun deleteOrder(
    dto: DeleteDto,
    orderId: Int,
    token: String,
  ): Result<HttpStatusCode>
}