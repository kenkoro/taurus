package com.kenkoro.taurus.client.feature.sewing.domain.repository

import com.kenkoro.taurus.client.feature.sewing.data.source.repository.UserRepositoryImpl
import com.kenkoro.taurus.client.feature.sewing.domain.model.request.LoginRequest
import com.kenkoro.taurus.client.feature.sewing.domain.model.response.AuthResponse
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

interface UserRepository {
  suspend fun login(request: LoginRequest): AuthResponse

  companion object {
    fun create(): UserRepositoryImpl {
      return UserRepositoryImpl(
        client = HttpClient(CIO) {
          install(Logging) {
            level = LogLevel.ALL
          }
          install(ContentNegotiation) {
            json()
          }
        }
      )
    }
  }
}