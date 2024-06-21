package com.kenkoro.taurus.client.feature.orders.presentation.screen.order_editor

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order_editor.util.OrderIdState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OrderEditorViewModel
  @Inject
  constructor(
    // TODO: Deps
  ) : ViewModel() {
    // TODO: Network call to check if the order is unique
    var isOrderIdUnique by mutableStateOf(false)
      private set

    var orderId by mutableStateOf(OrderIdState(orderId = null, unique = isOrderIdUnique))
      private set
  }