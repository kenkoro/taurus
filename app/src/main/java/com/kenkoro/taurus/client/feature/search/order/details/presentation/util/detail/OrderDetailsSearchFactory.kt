package com.kenkoro.taurus.client.feature.search.order.details.presentation.util.detail

import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.util.CustomerState
import com.kenkoro.taurus.client.feature.search.order.details.presentation.util.detail.strategies.OrderDetailCustomer
import com.kenkoro.taurus.client.feature.shared.states.TaurusTextFieldState

object OrderDetailsSearchFactory {
  fun getOrderDetailSearchStrategy(state: TaurusTextFieldState): OrderDetailStrategy? {
    return when (state) {
      is CustomerState -> OrderDetailCustomer()
      else -> null
    }
  }
}