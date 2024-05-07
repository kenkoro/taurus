package com.kenkoro.taurus.client.feature.sewing.data.source.remote.repository

import com.kenkoro.taurus.client.feature.sewing.data.source.remote.api.LoginRemoteApi
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.LoginDto
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.TokenDto

class LoginRepositoryImpl(
  private val api: LoginRemoteApi,
) : LoginRepository {
  override suspend fun login(dto: LoginDto): Result<TokenDto> =
    runCatching {
      api.login(dto)
    }
}