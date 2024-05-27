package com.kenkoro.taurus.client.feature.profile.data.remote.api

import com.kenkoro.taurus.client.feature.profile.data.remote.dto.NewUserDto
import com.kenkoro.taurus.client.feature.profile.data.remote.dto.UserDto
import com.kenkoro.taurus.client.feature.shared.data.remote.dto.DeleteDto
import io.ktor.http.HttpStatusCode

interface UserRemoteApi {
  suspend fun addNewUser(
    dto: NewUserDto,
    // Later add here a token
  ): UserDto

  suspend fun getUser(
    subject: String,
    token: String,
  ): UserDto

  suspend fun editUser(
    dto: NewUserDto,
    subject: String,
    editorSubject: String,
    token: String,
  ): HttpStatusCode

  suspend fun deleteUser(
    dto: DeleteDto,
    subject: String,
    token: String,
  ): HttpStatusCode
}