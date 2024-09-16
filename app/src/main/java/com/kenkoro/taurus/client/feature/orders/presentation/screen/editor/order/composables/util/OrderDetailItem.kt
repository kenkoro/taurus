package com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.composables.util

import com.kenkoro.taurus.client.feature.shared.states.TaurusTextFieldState

data class OrderDetailItem(
  val state: TaurusTextFieldState,
  val dropDownTitle: String,
)