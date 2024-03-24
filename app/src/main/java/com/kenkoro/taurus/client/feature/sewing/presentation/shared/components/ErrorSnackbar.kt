package com.kenkoro.taurus.client.feature.sewing.presentation.shared.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import com.kenkoro.taurus.client.core.local.LocalShape
import com.kenkoro.taurus.client.core.local.Shape

@Composable
fun ErrorSnackbar(
  modifier: Modifier = Modifier,
  snackbarData: SnackbarData,
) {
  CompositionLocalProvider(LocalShape provides Shape()) {
    val shape = LocalShape.current
    Snackbar(
      modifier = modifier,
      snackbarData = snackbarData,
      shape = RoundedCornerShape(shape.medium),
      containerColor = MaterialTheme.colorScheme.error,
      contentColor = MaterialTheme.colorScheme.onError,
      dismissActionContentColor = MaterialTheme.colorScheme.onError,
      actionColor = MaterialTheme.colorScheme.onError,
    )
  }
}