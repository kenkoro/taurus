package com.kenkoro.taurus.client.feature.sewing.data.source.remote.api

import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.LoginDto
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.TokenDto
import com.kenkoro.taurus.client.feature.sewing.data.util.Urls
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url

class LoginRemoteApiImpl(
  private val client: HttpClient,
) : LoginRemoteApi {
  override suspend fun login(dto: LoginDto): TokenDto =
    TokenDto(
      client.post {
        url(Urls.LOGIN)
        setBody(dto)
      }.body<TokenDto>().token
    )
}