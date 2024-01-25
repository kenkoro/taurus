package com.kenkoro.taurus.mobile_client.feature_login.data.source.remote

import com.kenkoro.taurus.mobile_client.feature_login.domain.model.AuthRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface DummyApi {
  @POST("auth/login")
  suspend fun authenticate(@Body request: AuthRequest): UserDto

  companion object {
    const val BASE_URL = "https://dummyjson.com"
  }
}