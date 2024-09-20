package com.kenkoro.taurus.client.feature.orders.presentation.screen.order.util

data class OrderScreenNavigator(
  val toProfileScreen: () -> Unit = {},
  val toOrderEditorScreen: (isInEdit: Boolean, subject: String) -> Unit,
)