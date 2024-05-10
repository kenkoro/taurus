package com.kenkoro.taurus.client.feature.sewing.data.source.remote.api

import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.DeleteDto
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.NewUserDto
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.UserDto
import io.ktor.http.HttpStatusCode

interface UserRemoteApi {
  suspend fun addNewUser(
    dto: NewUserDto,
    // Later add here a token
  ): UserDto

  suspend fun getUserBySubject(
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