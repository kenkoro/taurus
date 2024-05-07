package com.kenkoro.taurus.client.feature.sewing.data.source.remote.repository

import com.kenkoro.taurus.client.feature.sewing.data.source.remote.api.UserRemoteApi
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.DeleteDto
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.NewUserDto
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.UserDto
import io.ktor.http.HttpStatusCode

class UserRepositoryImpl(
  private val api: UserRemoteApi,
) : UserRepository {
  override suspend fun addNewUser(dto: NewUserDto): Result<UserDto> =
    runCatching {
      api.addNewUser(dto)
    }

  override suspend fun getUser(subject: String, token: String): Result<UserDto> =
    runCatching {
      api.getUser(subject, token)
    }

  override suspend fun editUser(
    dto: NewUserDto,
    subject: String,
    editorSubject: String,
    token: String
  ): Result<HttpStatusCode> =
    runCatching {
      api.editUser(dto, subject, editorSubject, token)
    }

  override suspend fun deleteUser(
    dto: DeleteDto,
    subject: String,
    token: String
  ): Result<HttpStatusCode> =
    runCatching {
      api.deleteUser(dto, subject, token)
    }
}