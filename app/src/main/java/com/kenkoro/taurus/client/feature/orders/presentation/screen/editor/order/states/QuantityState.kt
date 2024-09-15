package com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.states

import com.kenkoro.taurus.client.feature.shared.states.TaurusTextFieldState

class QuantityState(
  quantity: Int? = null,
) : TaurusTextFieldState(
    validator = ::isQuantityValid,
    errorFor = ::quantityValidationError,
  ) {
  init {
    quantity?.let {
      text = it.toString()
    }
  }
}

private fun quantityValidationError(
  quantity: String,
  errorMessage: String,
): String {
  return "$quantity $errorMessage"
}

private fun isQuantityValid(quantity: String): Boolean {
  return quantity.isNotBlank()
}