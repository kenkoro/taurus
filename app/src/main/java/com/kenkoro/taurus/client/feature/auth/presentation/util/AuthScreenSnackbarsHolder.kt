package com.kenkoro.taurus.client.feature.auth.presentation.util

import androidx.compose.material3.SnackbarResult

data class AuthScreenSnackbarsHolder(
  val internetConnectionError: suspend () -> SnackbarResult,
  val loginError: suspend () -> SnackbarResult,
)