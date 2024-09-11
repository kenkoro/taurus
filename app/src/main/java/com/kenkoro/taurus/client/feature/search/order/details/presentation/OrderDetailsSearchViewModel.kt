package com.kenkoro.taurus.client.feature.search.order.details.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.kenkoro.taurus.client.feature.search.order.details.presentation.util.detail.OrderDetailContext
import com.kenkoro.taurus.client.feature.search.order.details.presentation.util.detail.OrderDetailsSearchFactory
import com.kenkoro.taurus.client.feature.shared.states.TaurusTextFieldState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OrderDetailsSearchViewModel
  @Inject
  constructor() : ViewModel() {
    var selectedSearchState by mutableStateOf<TaurusTextFieldState?>(null)
    private val orderDetailContext = OrderDetailContext()

    fun changeOrderDetailsSearchBehavior(state: TaurusTextFieldState) {
      orderDetailContext.strategy(OrderDetailsSearchFactory.getOrderDetailSearchStrategy(state))
    }

    suspend fun fetch(): List<String> = orderDetailContext.fetch()
  }