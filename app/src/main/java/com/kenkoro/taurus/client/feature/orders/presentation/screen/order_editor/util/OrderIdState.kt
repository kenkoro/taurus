package com.kenkoro.taurus.client.feature.orders.presentation.screen.order_editor.util

import androidx.core.text.isDigitsOnly
import com.kenkoro.taurus.client.feature.shared.states.TaurusTextFieldState

class OrderIdState(
  orderId: Int? = null,
  unique: Boolean,
) : TaurusTextFieldState(
    validator = { isOrderIdValid(it, unique) },
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
  emptyTextFieldErrorMessage: String,
): String {
  return if (orderId.isBlank()) {
    emptyTextFieldErrorMessage
  } else {
    errorMessage
  }
}

private fun isOrderIdValid(
  orderId: String,
  unique: Boolean,
): Boolean {
  return orderId.isDigitsOnly() && unique
}