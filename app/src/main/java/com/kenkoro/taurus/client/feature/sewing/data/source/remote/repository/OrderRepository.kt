package com.kenkoro.taurus.client.feature.sewing.data.source.remote.repository

import com.kenkoro.taurus.client.feature.sewing.data.source.remote.api.OrderRemoteApi
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.DeleteDto
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.NewOrderDto
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.OrderDto
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.PaginatedOrdersDto
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
    dto: NewOrderDto,
    orderId: Int,
    editorSubject: String,
    token: String,
  ): Result<HttpStatusCode>

  suspend fun deleteOrder(
    dto: DeleteDto,
    orderId: Int,
    token: String,
  ): Result<HttpStatusCode>
}