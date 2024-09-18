package com.kenkoro.taurus.client.feature.shared.data

import com.kenkoro.taurus.client.feature.shared.data.remote.dto.TokenDto

interface ViewModelUtils {
  suspend fun auth(
    subject: String,
    password: String,
  ): Result<TokenDto>
}