package com.kenkoro.taurus.client.feature.orders.presentation.screen.order.util

import androidx.compose.material3.SnackbarHostState

data class SnackbarsHolder(
  val snackbarHostState: SnackbarHostState,
  val errorSnackbarHostState: SnackbarHostState,
  val internetErrorSnackbarHostState: SnackbarHostState,
)