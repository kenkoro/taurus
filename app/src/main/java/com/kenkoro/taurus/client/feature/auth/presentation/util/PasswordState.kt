package com.kenkoro.taurus.client.feature.auth.presentation.util

import com.kenkoro.taurus.client.feature.shared.states.TaurusTextFieldState

class PasswordState(
  password: String? = null,
) : TaurusTextFieldState(
    validator = ::isPasswordValid,
    errorFor = ::passwordValidationError,
  ) {
  init {
    password?.let {
      text = it
    }
  }
}

private fun passwordValidationError(
  password: String,
  errorMessage: String,
): String {
  return "$errorMessage!"
}

private fun isPasswordValid(password: String): Boolean = password.length >= 4