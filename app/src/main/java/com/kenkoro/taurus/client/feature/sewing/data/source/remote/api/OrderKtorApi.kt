package com.kenkoro.taurus.client.feature.sewing.data.source.remote.api

import com.kenkoro.taurus.client.feature.sewing.data.source.remote.api.OrderApi.Companion.token
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.request.Order
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.request.UpdateRequest
import com.kenkoro.taurus.client.feature.sewing.data.util.OrderDataType
import com.kenkoro.taurus.client.feature.sewing.data.util.Urls
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.http.parameters
import io.ktor.http.path

class OrderKtorApi(
  private val client: HttpClient
) : OrderApi {
  override suspend fun newOrder(request: Order): HttpResponse {
    return client.post {
      url {
        protocol = URLProtocol.HTTPS
        host = Urls.HOST
        path(Urls.Order.NEW_ORDER)
      }
      setBody(request)
      contentType(ContentType.Application.Json)
      headers {
        append("Authorization", "Bearer $token")
      }
    }
  }

  override suspend fun getOrder(orderId: Int): HttpResponse {
    return client.get {
      url {
        protocol = URLProtocol.HTTPS
        host = Urls.HOST
        path(Urls.Order.GET_ORDER)
        parameters {
          append("order_id", orderId.toString())
        }
      }
      contentType(ContentType.Application.Json)
      headers {
        append("Authorization", "Bearer $token")
      }
    }
  }

  override suspend fun getOrders(page: Int, perPage: Int): HttpResponse {
    return client.get {
      url {
        protocol = URLProtocol.HTTPS
        host = Urls.HOST
        path(Urls.Order.GET_ORDERS)
        parameters {
          append("page", page.toString())
          append("per_page", perPage.toString())
        }
      }
      contentType(ContentType.Application.Json)
      headers {
        append("Authorization", "Bearer $token")
      }
    }
  }

  override suspend fun updateOrderData(
    request: UpdateRequest,
    orderId: Int,
    data: OrderDataType
  ): HttpResponse {
    return client.put {
      url {
        protocol = URLProtocol.HTTPS
        host = Urls.HOST
        path("${Urls.Order.GET_ORDER}/$orderId/edit/${data.toUrl}")
      }
      setBody(request)
      contentType(ContentType.Application.Json)
      headers {
        append("Authorization", "Bearer $token")
      }
    }
  }

  override suspend fun deleteOrder(orderId: Int): HttpResponse {
    return client.delete {
      url {
        protocol = URLProtocol.HTTPS
        host = Urls.HOST
        path(Urls.Order.DELETE_ORDER)
        parameters {
          append("order_id", orderId.toString())
        }
      }
      contentType(ContentType.Application.Json)
      headers {
        append("Authorization", "Bearer $token")
      }
    }
  }
}