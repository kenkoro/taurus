package com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.util

import com.kenkoro.taurus.client.feature.shared.states.TaurusTextFieldState

data class OrderStatesHolder(
  val categoryState: TaurusTextFieldState = CategoryState(),
  val colorState: TaurusTextFieldState = ColorState(),
  val customerState: TaurusTextFieldState = CustomerState(),
  val modelState: TaurusTextFieldState = ModelState(),
  val orderIdState: TaurusTextFieldState = OrderIdState(),
  val quantityState: TaurusTextFieldState = QuantityState(),
  val sizeState: TaurusTextFieldState = SizeState(),
  val titleState: TaurusTextFieldState = TitleState(),
)