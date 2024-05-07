package com.kenkoro.taurus.client.feature.sewing.data.source.remote.repository

import com.kenkoro.taurus.client.feature.sewing.data.source.remote.api.UserRemoteApi
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.DeleteDto
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.NewUserDto
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.UserDto
import io.ktor.http.HttpStatusCode

interface UserRepository {
  companion object {
    fun create(api: UserRemoteApi): UserRepositoryImpl = UserRepositoryImpl(api)
  }

  suspend fun addNewUser(
    dto: NewUserDto,
    /**
     * For later, turn this on
     * token: String,
     */
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