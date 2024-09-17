package com.kenkoro.taurus.client.feature.auth.data.remote.repository

import com.kenkoro.taurus.client.feature.auth.data.remote.api.AuthRemoteApi
import com.kenkoro.taurus.client.feature.auth.data.remote.dto.AuthDto
import com.kenkoro.taurus.client.feature.shared.data.remote.dto.TokenDto

interface LoginRepository {
  companion object {
    fun create(api: AuthRemoteApi): LoginRepositoryImpl = LoginRepositoryImpl(api)
  }

  suspend fun logIn(dto: AuthDto): Result<TokenDto>
}