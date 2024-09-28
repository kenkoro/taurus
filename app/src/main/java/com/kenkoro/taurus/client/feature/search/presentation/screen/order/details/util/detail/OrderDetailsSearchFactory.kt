package com.kenkoro.taurus.client.feature.search.presentation.screen.order.details.util.detail

import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.states.CategoryState
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.states.ColorState
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.states.CustomerState
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.states.ModelState
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.states.SizeState
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.states.TitleState
import com.kenkoro.taurus.client.feature.search.presentation.screen.order.details.util.detail.strategies.OrderDetailCategory
import com.kenkoro.taurus.client.feature.search.presentation.screen.order.details.util.detail.strategies.OrderDetailColor
import com.kenkoro.taurus.client.feature.search.presentation.screen.order.details.util.detail.strategies.OrderDetailCustomer
import com.kenkoro.taurus.client.feature.search.presentation.screen.order.details.util.detail.strategies.OrderDetailModel
import com.kenkoro.taurus.client.feature.search.presentation.screen.order.details.util.detail.strategies.OrderDetailSize
import com.kenkoro.taurus.client.feature.search.presentation.screen.order.details.util.detail.strategies.OrderDetailTitle
import com.kenkoro.taurus.client.feature.shared.states.TaurusTextFieldState

object OrderDetailsSearchFactory {
  fun getOrderDetailSearchStrategy(state: TaurusTextFieldState): OrderDetailStrategy? {
    return when (state) {
      is CustomerState -> OrderDetailCustomer()
      is TitleState -> OrderDetailTitle()
      is ModelState -> OrderDetailModel()
      is SizeState -> OrderDetailSize()
      is ColorState -> OrderDetailColor()
      is CategoryState -> OrderDetailCategory()
      else -> null
    }
  }
}