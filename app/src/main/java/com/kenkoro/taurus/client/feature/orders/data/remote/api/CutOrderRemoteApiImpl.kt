package com.kenkoro.taurus.client.feature.orders.data.remote.api

import com.kenkoro.taurus.client.feature.orders.data.remote.dto.ActualCutOrdersQuantityDto
import com.kenkoro.taurus.client.feature.orders.data.remote.dto.CutOrderDto
import com.kenkoro.taurus.client.feature.orders.data.remote.dto.NewCutOrderDto
import com.kenkoro.taurus.client.feature.shared.Urls
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType

class CutOrderRemoteApiImpl(
  private val client: HttpClient,
) : CutOrderRemoteApi {
  override suspend fun addNewCutOrder(
    dto: NewCutOrderDto,
    token: String,
  ): CutOrderDto =
    client.post {
      url(Urls.ADD_NEW_CUT_ORDER)
      setBody(dto)
      contentType(ContentType.Application.Json)
      headers {
        append("Authorization", "Bearer $token")
      }
    }.body<CutOrderDto>()

  override suspend fun getActualCutOrdersQuantity(
    orderId: Int,
    token: String,
  ): ActualCutOrdersQuantityDto =
    client.get {
      url("${Urls.GET_ACTUAL_CUT_ORDERS_QUANTITY}/$orderId")
      contentType(ContentType.Application.Json)
      headers {
        append("Authorization", "Bearer $token")
      }
    }.body<ActualCutOrdersQuantityDto>()
}