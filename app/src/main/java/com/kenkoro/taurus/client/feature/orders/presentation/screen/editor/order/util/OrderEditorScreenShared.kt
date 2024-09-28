package com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.util

import com.kenkoro.taurus.client.core.connectivity.NetworkStatus
import com.kenkoro.taurus.client.feature.profile.domain.User
import com.kenkoro.taurus.client.feature.shared.states.TaurusTextFieldState

data class OrderEditorScreenShared(
  val user: User?,
  val network: NetworkStatus,
  val changeBehaviorOfOrderDetailsSearch: (TaurusTextFieldState) -> Unit = {},
)