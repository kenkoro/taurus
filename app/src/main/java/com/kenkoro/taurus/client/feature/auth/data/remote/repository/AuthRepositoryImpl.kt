package com.kenkoro.taurus.client.feature.auth.data.remote.repository

import com.kenkoro.taurus.client.feature.auth.data.remote.api.AuthRemoteApi
import com.kenkoro.taurus.client.feature.auth.data.remote.dto.AuthDto
import com.kenkoro.taurus.client.feature.shared.data.remote.dto.TokenDto

class AuthRepositoryImpl(
  private val api: AuthRemoteApi,
) : AuthRepository {
  override suspend fun logIn(dto: AuthDto): Result<TokenDto> =
    runCatching {
      api.logIn(dto)
    }
}