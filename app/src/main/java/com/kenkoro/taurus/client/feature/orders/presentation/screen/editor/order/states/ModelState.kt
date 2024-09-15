package com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.states

import com.kenkoro.taurus.client.feature.shared.states.TaurusTextFieldState

class ModelState(
  model: String? = null,
) : TaurusTextFieldState(
    validator = ::isModelValid,
    errorFor = ::modelValidationError,
  ) {
  init {
    model?.let {
      text = it
    }
  }
}

private fun modelValidationError(
  model: String,
  errorMessage: String,
): String {
  return "$model $errorMessage"
}

private fun isModelValid(model: String): Boolean {
  return model.isNotBlank()
}