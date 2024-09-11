package com.kenkoro.taurus.client.feature.search.order.details.presentation.util.detail

import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.util.CategoryState
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.util.ColorState
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.util.CustomerState
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.util.ModelState
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.util.SizeState
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.util.TitleState
import com.kenkoro.taurus.client.feature.search.order.details.presentation.util.detail.strategies.OrderDetailCategory
import com.kenkoro.taurus.client.feature.search.order.details.presentation.util.detail.strategies.OrderDetailColor
import com.kenkoro.taurus.client.feature.search.order.details.presentation.util.detail.strategies.OrderDetailCustomer
import com.kenkoro.taurus.client.feature.search.order.details.presentation.util.detail.strategies.OrderDetailModel
import com.kenkoro.taurus.client.feature.search.order.details.presentation.util.detail.strategies.OrderDetailSize
import com.kenkoro.taurus.client.feature.search.order.details.presentation.util.detail.strategies.OrderDetailTitle
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