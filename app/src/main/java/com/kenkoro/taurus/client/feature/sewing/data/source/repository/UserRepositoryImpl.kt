package com.kenkoro.taurus.client.feature.sewing.data.source.repository

import com.kenkoro.taurus.client.feature.sewing.data.source.remote.api.UserApi
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.request.LoginRequest
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.request.NewUserRequest
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.request.UpdateRequest
import com.kenkoro.taurus.client.feature.sewing.data.util.UserDataType
import io.ktor.client.statement.HttpResponse

class UserRepositoryImpl(
  private val userApi: UserApi,
) : UserRepository {
  fun token(token: String): UserRepository {
    UserApi.token(token)
    return this
  }

  override suspend fun login(request: LoginRequest): HttpResponse {
    return userApi.login(request)
  }

  override suspend fun newUser(request: NewUserRequest): HttpResponse {
    return userApi.newUser(request)
  }

  override suspend fun getUser(user: String): HttpResponse {
    return userApi.getUser(user)
  }

  override suspend fun updateUserData(
    request: UpdateRequest,
    user: String,
    data: UserDataType,
  ): HttpResponse {
    return userApi.updateUserData(request, user, data)
  }

  override suspend fun deleteUser(user: String): HttpResponse {
    return userApi.deleteUser(user)
  }
}