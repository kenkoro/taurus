package com.kenkoro.taurus.client.feature.sewing.data.source.repository

import com.kenkoro.taurus.client.feature.sewing.data.source.remote.api.UserApi
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.request.LoginRequestDto
import io.ktor.client.statement.HttpResponse

class UserRepositoryImpl(
  private val userApi: UserApi,
) : UserRepository {
  fun token(token: String): UserRepository {
    UserApi.token(token)
    return this
  }

  override suspend fun login(request: LoginRequestDto): HttpResponse {
    return userApi.login(request)
  }

  override suspend fun getUser(user: String): HttpResponse {
    return userApi.getUser(user)
  }
}