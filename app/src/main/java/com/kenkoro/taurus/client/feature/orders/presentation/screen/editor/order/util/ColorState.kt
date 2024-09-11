package com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.util

import com.kenkoro.taurus.client.feature.shared.states.TaurusTextFieldState

class ColorState(
  color: String? = null,
) : TaurusTextFieldState(
    validator = ::isColorValid,
    errorFor = ::colorValidationError,
  ) {
  init {
    color?.let {
      text = it
    }
  }
}

private fun colorValidationError(
  color: String,
  errorMessage: String,
): String {
  return "$color $errorMessage"
}

private fun isColorValid(color: String): Boolean {
  return color.isNotBlank()
}