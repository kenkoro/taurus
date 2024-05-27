package com.kenkoro.taurus.client.feature.login.data.remote.api

import com.kenkoro.taurus.client.feature.login.data.remote.dto.LoginDto
import com.kenkoro.taurus.client.feature.shared.data.remote.dto.TokenDto

interface LoginRemoteApi {
  suspend fun login(dto: LoginDto): TokenDto
}