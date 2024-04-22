package com.kenkoro.taurus.client.feature.sewing.presentation.screen.login.util

data class LoginCredentials(
  val subject: String,
  val password: String,
  val token: JwtToken,
)