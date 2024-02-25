package com.kenkoro.taurus.client.feature.sewing.data.source.repository

import com.kenkoro.taurus.client.feature.sewing.data.source.remote.api.UserApi
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.request.CreateUserRequest
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.request.DeleteUserRequest
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.request.LoginRequest
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.request.UpdateUserColumnRequest
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.response.GetUserResponse
import com.kenkoro.taurus.client.feature.sewing.data.util.UserDataType
import io.ktor.client.statement.HttpResponse

interface UserRepository {
  suspend fun login(request: LoginRequest): HttpResponse

  suspend fun getUser(): GetUserResponse

  suspend fun createUser(request: CreateUserRequest): HttpResponse

  suspend fun deleteUser(request: DeleteUserRequest): HttpResponse

  suspend fun updateUserData(
    request: UpdateUserColumnRequest,
    user: String,
    data: UserDataType,
  ): HttpResponse

  companion object {
    fun create(userApi: UserApi): UserRepositoryImpl {
      return UserRepositoryImpl(userApi)
    }
  }
}