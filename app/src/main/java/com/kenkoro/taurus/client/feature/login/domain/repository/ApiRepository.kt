package com.kenkoro.taurus.client.feature.login.domain.repository

import com.kenkoro.taurus.client.feature.login.data.source.remote.UserDto
import com.kenkoro.taurus.client.feature.login.domain.model.AuthRequest

interface ApiRepository {
  suspend fun auth(request: AuthRequest): UserDto
}