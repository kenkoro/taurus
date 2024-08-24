package com.kenkoro.taurus.client.feature.login.presentation.util

import com.kenkoro.taurus.client.feature.shared.data.remote.dto.TokenDto

data class LoginScreenRemoteHandler(
  val login: suspend (subject: String, password: String) -> Result<TokenDto>,
)