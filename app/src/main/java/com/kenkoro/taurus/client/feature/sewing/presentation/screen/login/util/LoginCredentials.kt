package com.kenkoro.taurus.client.feature.sewing.presentation.screen.login.util

import com.kenkoro.taurus.client.feature.sewing.presentation.shared.inline.JwtToken

data class LoginCredentials(
  val subject: String,
  val password: String,
  val token: JwtToken,
)