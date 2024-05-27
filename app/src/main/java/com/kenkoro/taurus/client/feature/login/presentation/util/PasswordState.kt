package com.kenkoro.taurus.client.feature.login.presentation.util

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
  emptyTextFieldErrorMessage: String,
): String {
  return if (password.isBlank()) {
    emptyTextFieldErrorMessage
  } else {
    "$errorMessage!"
  }
}

private fun isPasswordValid(password: String): Boolean = password.length >= 4