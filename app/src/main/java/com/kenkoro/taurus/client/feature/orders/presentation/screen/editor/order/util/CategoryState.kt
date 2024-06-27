package com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.util

import com.kenkoro.taurus.client.feature.shared.states.TaurusTextFieldState

class CategoryState(
  category: String? = null,
) : TaurusTextFieldState(
    validator = ::isCategoryValid,
    errorFor = ::categoryValidationError,
  ) {
  init {
    category?.let {
      text = category
    }
  }
}

private fun categoryValidationError(
  category: String,
  errorMessage: String,
): String {
  return "$category $errorMessage"
}

private fun isCategoryValid(category: String): Boolean {
  return category.isNotBlank() && category.first().isUpperCase()
}