package com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.util

import androidx.core.text.isDigitsOnly
import com.kenkoro.taurus.client.feature.shared.states.TaurusTextFieldState

class QuantityState(
  quantity: Int? = null,
) : TaurusTextFieldState() {
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
  return quantity.isNotBlank() && quantity.isDigitsOnly()
}