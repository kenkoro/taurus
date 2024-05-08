package com.kenkoro.taurus.client.feature.sewing.data.source.remote.api

import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.DeleteDto
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.NewOrderDto
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.OrderDto
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.PaginatedOrdersDto
import com.kenkoro.taurus.client.feature.sewing.data.util.Urls
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.parameters

class OrderRemoteApiException(message: String) : Exception(message)

class OrderRemoteApiImpl(
  private val client: HttpClient,
) : OrderRemoteApi {
  override suspend fun addNewOrder(
    dto: NewOrderDto,
    token: String,
  ): OrderDto =
    client.post {
      url(Urls.ADD_NEW_ORDER)
      setBody(dto)
      contentType(ContentType.Application.Json)
      headers {
        append("Authorization", "Bearer $token")
      }
    }.body<OrderDto>()

  override suspend fun getOrder(
    orderId: Int,
    token: String,
  ): OrderDto =
    client.get {
      url("${Urls.GET_ORDER}/$orderId")
      contentType(ContentType.Application.Json)
      headers {
        append("Authorization", "Bearer $token")
      }
    }.body<OrderDto>()

  override suspend fun getPaginatedOrders(
    page: Int,
    perPage: Int,
    token: String,
  ): PaginatedOrdersDto =
    client.get {
      url(Urls.GET_PAGINATED_ORDERS)
      contentType(ContentType.Application.Json)
      parameters {
        append("page", page.toString())
        append("per_page", perPage.toString())
      }
      headers {
        append("Authorization", "Bearer $token")
      }
    }.body<PaginatedOrdersDto>()

  override suspend fun editOrder(
    dto: NewOrderDto,
    orderId: Int,
    editorSubject: String,
    token: String,
  ): HttpStatusCode {
    val status =
      client.put {
        url(Urls.EDIT_ORDER)
        contentType(ContentType.Application.Json)
        setBody(dto)
        parameters {
          append("order_id", orderId.toString())
          append("editor_subject", editorSubject)
        }
        headers {
          append("Authorization", "Bearer $token")
        }
      }.status

    if (status != HttpStatusCode.OK) {
      throw OrderRemoteApiException("The editing of order ${dto.orderId} was unsuccessful")
    }
    return status
  }

  override suspend fun deleteOrder(
    dto: DeleteDto,
    orderId: Int,
    token: String,
  ): HttpStatusCode {
    val status =
      client.delete {
        url("${Urls.DELETE_ORDER}/$orderId")
        contentType(ContentType.Application.Json)
        setBody(dto)
        headers {
          append("Authorization", "Bearer $token")
        }
      }.status

    if (status != HttpStatusCode.OK) {
      throw OrderRemoteApiException("The deleting of order $orderId was unsuccessful")
    }
    return status
  }
}