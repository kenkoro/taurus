package com.kenkoro.taurus.client.feature.sewing.presentation.screen.login.util

import com.kenkoro.taurus.client.feature.sewing.presentation.shared.TaurusTextFieldState

class PasswordState(
  password: String? = null,
  private val errorMessage: String = "",
  private val emptyTextFieldErrorMessage: String = "",
) : TaurusTextFieldState(
    validator = ::isPasswordValid,
    errorFor = { text ->
      passwordValidationError(text, errorMessage, emptyTextFieldErrorMessage)
    },
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