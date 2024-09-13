package com.kenkoro.taurus.client.feature.orders.data.remote.repository

import com.kenkoro.taurus.client.feature.orders.data.remote.api.OrderRemoteApi
import com.kenkoro.taurus.client.feature.orders.data.remote.dto.EditOrderDto
import com.kenkoro.taurus.client.feature.orders.data.remote.dto.NewOrderDto
import com.kenkoro.taurus.client.feature.orders.data.remote.dto.OrderDto
import com.kenkoro.taurus.client.feature.orders.data.remote.dto.PaginatedOrdersDto
import com.kenkoro.taurus.client.feature.shared.data.remote.dto.DeleteDto
import io.ktor.http.HttpStatusCode

class OrderRepositoryImpl(
  private val api: OrderRemoteApi,
) : OrderRepository {
  override suspend fun addNewOrder(
    dto: NewOrderDto,
    token: String,
  ): Result<OrderDto> =
    runCatching {
      api.addNewOrder(dto, token)
    }

  override suspend fun getOrder(
    orderId: Int,
    token: String,
  ): Result<OrderDto> =
    runCatching {
      api.getOrder(orderId, token)
    }

  override suspend fun getPaginatedOrders(
    page: Int,
    perPage: Int,
    token: String,
  ): PaginatedOrdersDto = api.getPaginatedOrders(page, perPage, token)

  override suspend fun editOrder(
    dto: EditOrderDto,
    editorSubject: String,
    token: String,
  ): Result<HttpStatusCode> =
    runCatching {
      api.editOrder(dto, editorSubject, token)
    }

  override suspend fun deleteOrder(
    dto: DeleteDto,
    orderId: Int,
    token: String,
  ): Result<HttpStatusCode> =
    runCatching {
      api.deleteOrder(dto, orderId, token)
    }
}