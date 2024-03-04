package com.kenkoro.taurus.client.feature.sewing.data.source.repository

import com.kenkoro.taurus.client.feature.sewing.data.source.remote.api.UserApi
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.request.CreateUserRequest
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.request.DeleteUserRequest
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.request.LoginRequest
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.request.UpdateUserColumnRequest
import com.kenkoro.taurus.client.feature.sewing.data.util.UserDataType
import io.ktor.client.statement.HttpResponse

class UserRepositoryImpl(
  private val userApi: UserApi,
) : UserRepository {
  override suspend fun login(request: LoginRequest): HttpResponse {
    return userApi.login(request)
  }

  override suspend fun getUser(): HttpResponse {
    return userApi.getUser()
  }

  override suspend fun createUser(request: CreateUserRequest): HttpResponse {
    return userApi.createUser(request)
  }

  override suspend fun deleteUser(request: DeleteUserRequest): HttpResponse {
    return userApi.deleteUser(request)
  }

  override suspend fun updateUserData(
    request: UpdateUserColumnRequest,
    user: String,
    data: UserDataType,
  ): HttpResponse {
    return userApi.updateUserData(request, user, data)
  }
}