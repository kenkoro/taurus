package com.kenkoro.taurus.client.feature.sewing.data.source.remote.api

import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.DeleteDto
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.NewOrderDto
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.OrderDto
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.PaginatedOrdersDto
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
    orderId: Int,
    editorSubject: String,
    token: String,
  ): HttpStatusCode

  suspend fun deleteOrder(
    dto: DeleteDto,
    orderId: Int,
    token: String,
  ): HttpStatusCode
}