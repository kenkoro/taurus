package com.kenkoro.taurus.client.feature.auth.data.remote.api

import com.kenkoro.taurus.client.feature.auth.data.remote.dto.LoginDto
import com.kenkoro.taurus.client.feature.shared.data.remote.dto.TokenDto

interface LoginRemoteApi {
  suspend fun login(dto: LoginDto): TokenDto
}