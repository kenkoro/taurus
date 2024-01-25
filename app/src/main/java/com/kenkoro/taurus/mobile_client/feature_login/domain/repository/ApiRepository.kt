package com.kenkoro.taurus.mobile_client.feature_login.domain.repository

import com.kenkoro.taurus.mobile_client.feature_login.data.source.remote.UserDto
import com.kenkoro.taurus.mobile_client.feature_login.domain.model.AuthRequest

interface ApiRepository {
  suspend fun auth(request: AuthRequest): UserDto
}