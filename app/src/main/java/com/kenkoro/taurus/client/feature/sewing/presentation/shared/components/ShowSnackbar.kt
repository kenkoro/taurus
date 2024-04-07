package com.kenkoro.taurus.client.feature.sewing.presentation.shared.components

import android.annotation.SuppressLint
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import com.kenkoro.taurus.client.R
import kotlinx.coroutines.delay

@SuppressLint("ComposableNaming")
@Composable
fun <T> showSnackbar(
  snackbarHostState: SnackbarHostState,
  key: T,
  message: String,
  onDismissed: suspend () -> Unit = {},
  onActionPerformed: suspend () -> Unit = {},
  actionLabel: String? = stringResource(id = R.string.ok),
  delayInMillis: Long = 100L,
  duration: SnackbarDuration = SnackbarDuration.Indefinite,
  extraCondition: Boolean = true,
) {
  LaunchedEffect(key) {
    if (extraCondition) {
      delay(delayInMillis)
      val snackbarResult =
        snackbarHostState.showSnackbar(
          message = message,
          actionLabel = actionLabel,
          withDismissAction = false,
          duration = duration,
        )

      when (snackbarResult) {
        SnackbarResult.Dismissed -> onDismissed()
        SnackbarResult.ActionPerformed -> onActionPerformed()
      }
    }
  }
}