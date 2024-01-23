package com.kenkoro.taurus.mobile_client.feature_login.data.source

import com.kenkoro.taurus.mobile_client.feature_login.domain.model.AuthRequest
import com.kenkoro.taurus.mobile_client.feature_login.domain.model.User
import retrofit2.http.Body
import retrofit2.http.POST

interface TaurusApi {
  @POST("auth/login")
  suspend fun auth(@Body request: AuthRequest): User
}