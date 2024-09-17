package com.kenkoro.taurus.client.feature.auth.data.remote.api

import com.kenkoro.taurus.client.feature.auth.data.remote.dto.AuthDto
import com.kenkoro.taurus.client.feature.shared.Urls
import com.kenkoro.taurus.client.feature.shared.data.remote.dto.TokenDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType

class AuthRemoteApiImpl(
  private val client: HttpClient,
) : AuthRemoteApi {
  override suspend fun logIn(dto: AuthDto): TokenDto =
    client.post {
      url(Urls.LOGIN)
      contentType(ContentType.Application.Json)
      setBody(dto)
    }.body<TokenDto>()
}