package com.kenkoro.taurus.client.feature.sewing.data.source.remote.api

import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.LoginDto
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.TokenDto

interface LoginRemoteApi {
  suspend fun login(
    dto: LoginDto
  ): TokenDto
}