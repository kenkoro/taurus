package com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.util

import com.kenkoro.taurus.client.feature.shared.states.TaurusTextFieldState

object TextFieldValidationService {
  private fun check(state: TaurusTextFieldState) {
    state.isFocusedOnce = true
    state.enableShowErrors()
  }

  fun checkAll(orderStatesHolder: OrderStatesHolder) {
    check(orderStatesHolder.customerState)
    check(orderStatesHolder.colorState)
    check(orderStatesHolder.modelState)
    check(orderStatesHolder.sizeState)
    check(orderStatesHolder.quantityState)
    check(orderStatesHolder.categoryState)
    check(orderStatesHolder.titleState)
  }
}