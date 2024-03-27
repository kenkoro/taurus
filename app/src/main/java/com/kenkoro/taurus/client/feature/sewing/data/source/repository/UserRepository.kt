package com.kenkoro.taurus.client.feature.sewing.data.source.repository

import com.kenkoro.taurus.client.feature.sewing.data.source.remote.api.UserApi
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.request.LoginRequest
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.request.NewUserRequest
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.request.UpdateRequest
import com.kenkoro.taurus.client.feature.sewing.data.util.UserDataType
import io.ktor.client.statement.HttpResponse

interface UserRepository {
  companion object {
    fun create(userApi: UserApi): UserRepositoryImpl {
      return UserRepositoryImpl(userApi)
    }
  }

  suspend fun login(request: LoginRequest): HttpResponse

  suspend fun newUser(request: NewUserRequest): HttpResponse

  suspend fun getUser(user: String): HttpResponse

  suspend fun updateUserData(
    request: UpdateRequest,
    user: String,
    data: UserDataType,
  ): HttpResponse

  suspend fun deleteUser(user: String): HttpResponse
}