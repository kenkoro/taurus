package com.kenkoro.taurus.client.feature.profile.data.remote.repository

import com.kenkoro.taurus.client.feature.profile.data.remote.api.UserRemoteApi
import com.kenkoro.taurus.client.feature.profile.data.remote.dto.NewUserDto
import com.kenkoro.taurus.client.feature.profile.data.remote.dto.UserDto
import com.kenkoro.taurus.client.feature.shared.data.remote.dto.DeleteDto
import io.ktor.http.HttpStatusCode

interface UserRepository {
  companion object {
    fun create(api: UserRemoteApi): UserRepositoryImpl = UserRepositoryImpl(api)
  }

  suspend fun addNewUser(
    dto: NewUserDto,
    // TODO: Add here a jwt token
  ): Result<UserDto>

  suspend fun getUser(
    subject: String,
    token: String,
  ): Result<UserDto>

  suspend fun editUser(
    dto: NewUserDto,
    subject: String,
    editorSubject: String,
    token: String,
  ): Result<HttpStatusCode>

  suspend fun deleteUser(
    dto: DeleteDto,
    subject: String,
    token: String,
  ): Result<HttpStatusCode>
}