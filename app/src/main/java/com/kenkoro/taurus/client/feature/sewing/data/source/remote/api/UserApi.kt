package com.kenkoro.taurus.client.feature.sewing.data.source.remote.api

import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.request.LoginRequestDto
import io.ktor.client.statement.HttpResponse

interface UserApi {
  companion object {
    var token: String = ""

    fun token(token: String) {
      UserApi.token = token
    }
  }

  suspend fun login(request: LoginRequestDto): HttpResponse

  suspend fun getUser(user: String): HttpResponse
}