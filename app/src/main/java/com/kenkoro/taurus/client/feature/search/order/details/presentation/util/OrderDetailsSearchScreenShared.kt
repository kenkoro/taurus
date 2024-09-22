package com.kenkoro.taurus.client.feature.search.order.details.presentation.util

import com.kenkoro.taurus.client.feature.shared.states.TaurusTextFieldState

data class OrderDetailsSearchScreenShared(
  val selectedDropDownState: TaurusTextFieldState,
  val selectDropDown: (TaurusTextFieldState) -> Unit,
  val fetch: suspend (searchRequest: String) -> List<String>,
)