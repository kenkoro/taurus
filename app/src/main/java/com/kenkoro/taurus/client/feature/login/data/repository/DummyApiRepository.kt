package com.kenkoro.taurus.client.feature.login.data.repository

import com.kenkoro.taurus.client.feature.login.data.source.remote.DummyApi
import com.kenkoro.taurus.client.feature.login.data.source.remote.UserDto
import com.kenkoro.taurus.client.feature.login.domain.model.AuthRequest
import com.kenkoro.taurus.client.feature.login.domain.repository.ApiRepository

class DummyApiRepository(
  private val api: DummyApi
) : ApiRepository {
  override suspend fun auth(request: AuthRequest): UserDto {
    return api.authenticate(request)
  }
}