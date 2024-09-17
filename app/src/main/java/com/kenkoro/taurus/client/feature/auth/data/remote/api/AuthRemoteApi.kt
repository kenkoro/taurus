package com.kenkoro.taurus.client.feature.auth.data.remote.api

import com.kenkoro.taurus.client.feature.auth.data.remote.dto.AuthDto
import com.kenkoro.taurus.client.feature.shared.data.remote.dto.TokenDto

interface AuthRemoteApi {
  suspend fun logIn(dto: AuthDto): TokenDto
}