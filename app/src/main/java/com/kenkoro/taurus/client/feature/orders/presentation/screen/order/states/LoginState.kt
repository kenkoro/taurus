package com.kenkoro.taurus.client.feature.orders.presentation.screen.order.states

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

enum class LoginState {
  Success,
  NotLoggedYet,
  ;

  fun isSuccess(): Boolean {
    return this == Success
  }
}