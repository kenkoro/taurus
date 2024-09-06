package com.kenkoro.taurus.client.feature.orders.presentation.screen.order.util

import androidx.compose.material3.SnackbarResult

data class OrderScreenSnackbarsHolder(
  val notImplementedError: suspend () -> SnackbarResult,
  val loginError: suspend () -> SnackbarResult,
  val internetConnectionError: suspend () -> SnackbarResult,
  val getPaginatedOrdersError: suspend () -> SnackbarResult,
  val accessToOrdersError: suspend () -> SnackbarResult,
  val apiError: suspend () -> SnackbarResult,
  val orderWasDeleted: suspend (orderId: Int) -> SnackbarResult,
)