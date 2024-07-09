package com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.util

import androidx.core.text.isDigitsOnly
import com.kenkoro.taurus.client.feature.shared.states.TaurusTextFieldState

@Deprecated("Due to auto-inc order id")
class OrderIdState(
  orderId: Int? = null,
) : TaurusTextFieldState(
    validator = ::isOrderIdValid,
    errorFor = ::orderIdValidationError,
  ) {
  init {
    orderId?.let {
      text = it.toString()
    }
  }
}

private fun orderIdValidationError(
  orderId: String,
  errorMessage: String,
): String {
  return "$orderId $errorMessage"
}

private fun isOrderIdValid(orderId: String): Boolean {
  return orderId.isDigitsOnly() && orderId.isNotBlank()
}