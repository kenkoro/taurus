package com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.util

import androidx.compose.material3.SnackbarResult

data class OrderEditorScreenSnackbarsHolder(
  val failedOrderEditorValidation: suspend () -> SnackbarResult = { SnackbarResult.Dismissed },
  val apiError: suspend () -> SnackbarResult = { SnackbarResult.Dismissed },
)