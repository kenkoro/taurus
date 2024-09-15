package com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.states

import com.kenkoro.taurus.client.feature.shared.states.TaurusTextFieldState

class TitleState(
  title: String? = null,
) : TaurusTextFieldState(
    validator = ::isTitleValid,
    errorFor = ::titleValidationError,
  ) {
  init {
    title?.let {
      text = title
    }
  }
}

private fun titleValidationError(
  title: String,
  errorMessage: String,
): String {
  return "$title $errorMessage"
}

private fun isTitleValid(title: String): Boolean {
  return title.isNotBlank()
}