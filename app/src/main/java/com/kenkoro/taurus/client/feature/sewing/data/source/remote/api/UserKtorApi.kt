package com.kenkoro.taurus.client.feature.sewing.data.source.remote.api

import com.kenkoro.taurus.client.feature.sewing.data.source.remote.api.UserApi.Companion.token
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.request.CreateUserRequest
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.request.DeleteUserRequest
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.request.LoginRequest
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.request.UpdateUserColumnRequest
import com.kenkoro.taurus.client.feature.sewing.data.util.Urls
import com.kenkoro.taurus.client.feature.sewing.data.util.UserDataType
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
import io.ktor.http.path

class UserKtorApi(
  private val client: HttpClient,
) : UserApi {
  override suspend fun login(request: LoginRequest): HttpResponse {
    return client.post {
      url {
        protocol = URLProtocol.HTTPS
        host = Urls.HOST
        path(Urls.LOGIN)
      }
      setBody(request)
      contentType(ContentType.Application.Json)
    }
  }

  override suspend fun getUser(user: String): HttpResponse {
    return client.get {
      url {
        protocol = URLProtocol.HTTPS
        host = Urls.HOST
        path("${Urls.GET_USER}/@$user")
      }
      contentType(ContentType.Application.Json)
      headers {
        append("Authorization", "Bearer $token")
      }
    }
  }

  override suspend fun createUser(request: CreateUserRequest): HttpResponse {
    return client.post {
      url {
        protocol = URLProtocol.HTTPS
        host = Urls.HOST
        path(Urls.CREATE_USER)
      }
      setBody(request)
      contentType(ContentType.Application.Json)
    }
  }

  override suspend fun deleteUser(request: DeleteUserRequest): HttpResponse {
    return client.delete {
      url {
        protocol = URLProtocol.HTTPS
        host = Urls.HOST
        path(Urls.DELETE_USER)
      }
      setBody(request)
      contentType(ContentType.Application.Json)
      headers {
        append("Authorization", "Bearer $token")
      }
    }
  }

  override suspend fun updateUserData(
    request: UpdateUserColumnRequest,
    user: String,
    data: UserDataType,
  ): HttpResponse {
    return client.put {
      url {
        protocol = URLProtocol.HTTPS
        host = Urls.HOST
        path("${Urls.GET_USER}/$user/edit/${data.toUrl}")
      }
      setBody(request)
      contentType(ContentType.Application.Json)
      headers {
        append("Authorization", "Bearer $token")
      }
    }
  }
}