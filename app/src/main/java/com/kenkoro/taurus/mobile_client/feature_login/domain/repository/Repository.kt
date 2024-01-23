package com.kenkoro.taurus.mobile_client.feature_login.domain.repository

import com.kenkoro.taurus.mobile_client.feature_login.domain.model.AuthRequest
import com.kenkoro.taurus.mobile_client.feature_login.domain.model.User

interface Repository {
  suspend fun auth(request: AuthRequest): User
}