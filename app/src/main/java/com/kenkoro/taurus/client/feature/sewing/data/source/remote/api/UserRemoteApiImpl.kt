package com.kenkoro.taurus.client.feature.sewing.data.source.remote.api

import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.DeleteDto
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.NewUserDto
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.UserDto
import com.kenkoro.taurus.client.feature.sewing.data.util.Urls
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.parameters

class UserRemoteApiException(message: String) : Exception(message)

class UserRemoteApiImpl(
  private val client: HttpClient,
) : UserRemoteApi {
  override suspend fun addNewUser(dto: NewUserDto): UserDto =
    client.post {
      url(Urls.ADD_NEW_USER)
      contentType(ContentType.Application.Json)
      setBody(dto)
      /**
       * For later, turn this on
       * headers {
       *   append("Authorization", "Bearer $token")
       * }
       */
    }.body<UserDto>()

  override suspend fun getUserBySubject(
    subject: String,
    token: String,
  ): UserDto =
    client.get {
      url("${Urls.GET_USER}/$subject")
      contentType(ContentType.Application.Json)
      headers {
        append("Authorization", "Bearer $token")
      }
    }.body<UserDto>()

  override suspend fun editUser(
    dto: NewUserDto,
    subject: String,
    editorSubject: String,
    token: String,
  ): HttpStatusCode {
    val status =
      client.put {
        url(Urls.EDIT_USER)
        contentType(ContentType.Application.Json)
        setBody(dto)
        parameters {
          append("subject", subject)
          append("editor_subject", editorSubject)
        }
        headers {
          append("Authorization", "Bearer $token")
        }
      }.status

    if (status != HttpStatusCode.OK) {
      throw UserRemoteApiException("The editing of user ${dto.subject} was unsuccessful")
    }
    return status
  }

  override suspend fun deleteUser(
    dto: DeleteDto,
    subject: String,
    token: String,
  ): HttpStatusCode {
    val status =
      client.delete {
        url("${Urls.DELETE_USER}/$subject")
        contentType(ContentType.Application.Json)
        setBody(dto)
        headers {
          append("Authorization", "Bearer $token")
        }
      }.status

    if (status != HttpStatusCode.OK) {
      throw UserRemoteApiException("The editing of user $subject was unsuccessful")
    }
    return status
  }
}