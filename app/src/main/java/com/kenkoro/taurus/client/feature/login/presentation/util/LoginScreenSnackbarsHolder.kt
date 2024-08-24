package com.kenkoro.taurus.client.feature.login.presentation.util

import androidx.compose.material3.SnackbarResult

data class LoginScreenSnackbarsHolder(
  val internetConnectionError: suspend () -> SnackbarResult,
  val loginError: suspend () -> SnackbarResult,
)