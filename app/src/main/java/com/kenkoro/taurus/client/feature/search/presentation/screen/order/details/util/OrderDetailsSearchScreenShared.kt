package com.kenkoro.taurus.client.feature.search.presentation.screen.order.details.util

import com.kenkoro.taurus.client.feature.shared.states.TaurusTextFieldState

data class OrderDetailsSearchScreenShared(
  val selectedDropDownState: TaurusTextFieldState,
  val selectDropDown: (TaurusTextFieldState) -> Unit,
  val fetch: suspend (searchRequest: String) -> List<String>,
)