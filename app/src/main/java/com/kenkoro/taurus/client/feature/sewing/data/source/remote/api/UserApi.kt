package com.kenkoro.taurus.client.feature.sewing.data.source.remote.api

import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.request.CreateUserRequest
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.request.DeleteUserRequest
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.request.LoginRequest
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.request.UpdateUserColumnRequest
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.response.GetUserResponse
import com.kenkoro.taurus.client.feature.sewing.data.util.UserDataType
import io.ktor.client.statement.HttpResponse

interface UserApi {
  suspend fun login(request: LoginRequest): HttpResponse

  suspend fun getUser(): GetUserResponse

  suspend fun createUser(request: CreateUserRequest): HttpResponse

  suspend fun deleteUser(request: DeleteUserRequest): HttpResponse

  suspend fun updateUserData(
    request: UpdateUserColumnRequest,
    user: String,
    data: UserDataType,
  ): HttpResponse
}