package com.kenkoro.taurus.client.feature.sewing.data.source.remote.repository

import com.kenkoro.taurus.client.feature.sewing.data.source.remote.api.OrderRemoteApi
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.DeleteDto
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.NewOrderDto
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.OrderDto
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.PaginatedOrdersDto
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
    dto: NewOrderDto,
    orderId: Int,
    editorSubject: String,
    token: String,
  ): Result<HttpStatusCode> =
    runCatching {
      api.editOrder(dto, orderId, editorSubject, token)
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