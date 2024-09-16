package com.kenkoro.taurus.client.feature.orders.presentation.screen.order.states

enum class AuthStatus {
  Success,
  WaitingForAuth,
  Failure,
  ;

  val isSuccess: Boolean
    get() = this == Success
}