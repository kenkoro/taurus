package com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.states

import com.kenkoro.taurus.client.feature.shared.states.TaurusTextFieldState

class CustomerState(
  customer: String? = null,
) : TaurusTextFieldState(
    validator = ::isCustomerValid,
    errorFor = ::customerValidationError,
  ) {
  init {
    customer?.let {
      this.text = it
    }
  }
}

private fun customerValidationError(
  customer: String,
  errorMessage: String,
): String {
  return "$customer $errorMessage"
}

private fun isCustomerValid(customer: String): Boolean {
  return customer.isNotBlank()
}