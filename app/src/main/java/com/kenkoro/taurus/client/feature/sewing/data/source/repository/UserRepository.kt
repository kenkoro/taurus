package com.kenkoro.taurus.client.feature.sewing.data.source.repository

import com.kenkoro.taurus.client.feature.sewing.data.source.remote.api.UserApi
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.request.LoginRequestDto
import io.ktor.client.statement.HttpResponse

interface UserRepository {
  companion object {
    fun create(userApi: UserApi): UserRepositoryImpl {
      return UserRepositoryImpl(userApi)
    }
  }

  suspend fun login(request: LoginRequestDto): HttpResponse

  suspend fun getUser(user: String): HttpResponse
}