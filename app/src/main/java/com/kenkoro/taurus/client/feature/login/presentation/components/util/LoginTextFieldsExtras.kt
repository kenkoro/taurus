package com.kenkoro.taurus.client.feature.login.presentation.components.util

data class LoginTextFieldsExtras(
  val isAuthenticating: Boolean,
  val whenLoginSubmitted: (subject: String, password: String) -> Unit,
)