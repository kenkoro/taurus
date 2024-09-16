package com.kenkoro.taurus.client.feature.auth.presentation.components.util

data class AuthExtras(
  val isAuthenticating: Boolean,
  val whenLoginSubmitted: (subject: String, password: String) -> Unit,
)