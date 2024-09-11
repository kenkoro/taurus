package com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.util

import androidx.core.text.isDigitsOnly

private fun orderIdValidationError(
  orderId: String,
  errorMessage: String,
): String {
  return "$orderId $errorMessage"
}

private fun isOrderIdValid(orderId: String): Boolean {
  return orderId.isDigitsOnly() && orderId.isNotBlank()
}