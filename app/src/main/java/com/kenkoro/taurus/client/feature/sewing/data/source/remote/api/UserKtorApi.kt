package com.kenkoro.taurus.client.feature.sewing.data.source.remote.api

import com.kenkoro.taurus.client.feature.sewing.data.source.remote.api.UserApi.Companion.token
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.request.LoginRequestDto
import com.kenkoro.taurus.client.feature.sewing.data.util.Urls
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.http.path

class UserKtorApi(
  private val client: HttpClient,
) : UserApi {
  override suspend fun login(request: LoginRequestDto): HttpResponse =
    client.post {
      url {
        protocol = URLProtocol.HTTPS
        host = Urls.HOST
        path(Urls.User.LOGIN)
      }
      setBody(request)
      contentType(ContentType.Application.Json)
    }

  override suspend fun getUser(user: String): HttpResponse =
    client.get {
      url {
        protocol = URLProtocol.HTTPS
        host = Urls.HOST
        path("${Urls.User.GET_USER}/$user")
      }
      contentType(ContentType.Application.Json)
      headers {
        append("Authorization", "Bearer $token")
      }
    }
}