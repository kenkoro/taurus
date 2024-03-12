package com.kenkoro.taurus.client.feature.sewing.presentation.dashboard.screen.components

import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import com.kenkoro.taurus.client.R

@Composable
fun ErrorSnackbar(
  snackbarHostState: SnackbarHostState,
  message: String,
  onDismissed: () -> Unit = {},
  onActionPerformed: () -> Unit,
) {
  val actionLabel = stringResource(id = R.string.retry)
  LaunchedEffect(Unit) {
    val snackbarResult =
      snackbarHostState.showSnackbar(
        message = message,
        actionLabel = actionLabel,
        withDismissAction = false,
      )

    when (snackbarResult) {
      SnackbarResult.Dismissed -> onDismissed()
      SnackbarResult.ActionPerformed -> onActionPerformed()
    }
  }
}