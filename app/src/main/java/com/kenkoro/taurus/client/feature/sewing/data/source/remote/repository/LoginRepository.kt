package com.kenkoro.taurus.client.feature.sewing.data.source.remote.repository

import com.kenkoro.taurus.client.feature.sewing.data.source.remote.api.LoginRemoteApi
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.LoginDto
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.TokenDto

interface LoginRepository {
  companion object {
    fun create(api: LoginRemoteApi): LoginRepositoryImpl = LoginRepositoryImpl(api)
  }

  suspend fun login(dto: LoginDto): Result<TokenDto>
}