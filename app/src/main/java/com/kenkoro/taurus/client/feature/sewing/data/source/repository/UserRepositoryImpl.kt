package com.kenkoro.taurus.client.feature.sewing.data.source.repository

import com.kenkoro.taurus.client.feature.sewing.data.core.Urls
import com.kenkoro.taurus.client.feature.sewing.domain.model.request.LoginRequest
import com.kenkoro.taurus.client.feature.sewing.domain.model.response.AuthResponse
import com.kenkoro.taurus.client.feature.sewing.domain.repository.UserRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.http.path

class UserRepositoryImpl(
  private val client: HttpClient
) : UserRepository {
  override suspend fun login(request: LoginRequest): AuthResponse {
    /**
     * TODO: Add call logging & catch different request's exceptions
     * 1. RedirectResponseException - 3xx responses
     * 2. ClientResponseException - 4xx responses
     * 3. ServerResponseException - 5xx responses
     */
    return client.post {
      url {
        protocol = URLProtocol.HTTPS
        host = Urls.HOST
        path(Urls.LOGIN)
      }
      setBody(request)
      contentType(ContentType.Application.Json)
    }.body<AuthResponse>()
  }
}