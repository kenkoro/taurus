package com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.util

import com.kenkoro.taurus.client.feature.shared.states.TaurusTextFieldState

data class OrderEditorScreenNavigator(
  val navUp: () -> Unit = {},
  val toOrderDetailsSearchScreen: (selectedDetail: TaurusTextFieldState) -> Unit,
)