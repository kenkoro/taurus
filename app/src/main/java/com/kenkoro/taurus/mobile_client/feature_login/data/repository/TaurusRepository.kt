package com.kenkoro.taurus.mobile_client.feature_login.data.repository

import com.kenkoro.taurus.mobile_client.feature_login.data.source.TaurusApi
import com.kenkoro.taurus.mobile_client.feature_login.domain.model.AuthRequest
import com.kenkoro.taurus.mobile_client.feature_login.domain.model.User
import com.kenkoro.taurus.mobile_client.feature_login.domain.repository.Repository

class TaurusRepository(
  private val api: TaurusApi
) : Repository {
  override suspend fun auth(request: AuthRequest): User {
    return api.auth(request)
  }
}