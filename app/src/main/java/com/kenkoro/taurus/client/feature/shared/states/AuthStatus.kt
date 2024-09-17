package com.kenkoro.taurus.client.feature.shared.states

enum class AuthStatus {
  WaitingForAuth,
  Success,
  Failure,
  ;

  val isSuccess: Boolean
    get() = this == Success
}