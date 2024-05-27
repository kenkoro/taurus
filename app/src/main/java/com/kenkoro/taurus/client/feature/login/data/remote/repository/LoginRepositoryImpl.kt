package com.kenkoro.taurus.client.feature.login.data.remote.repository

import com.kenkoro.taurus.client.feature.login.data.remote.api.LoginRemoteApi
import com.kenkoro.taurus.client.feature.login.data.remote.dto.LoginDto
import com.kenkoro.taurus.client.feature.shared.data.remote.dto.TokenDto

class LoginRepositoryImpl(
  private val api: LoginRemoteApi,
) : LoginRepository {
  override suspend fun login(dto: LoginDto): Result<TokenDto> =
    runCatching {
      api.login(dto)
    }
}