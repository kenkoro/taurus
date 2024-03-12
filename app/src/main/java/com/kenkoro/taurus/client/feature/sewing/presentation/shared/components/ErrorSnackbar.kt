package com.kenkoro.taurus.client.feature.sewing.presentation.shared.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ErrorSnackbar(
  modifier: Modifier = Modifier,
  snackbarData: SnackbarData,
) {
  Snackbar(
    modifier = modifier,
    snackbarData = snackbarData,
    shape = RoundedCornerShape(30.dp),
    containerColor = MaterialTheme.colorScheme.error,
    contentColor = MaterialTheme.colorScheme.onError,
    dismissActionContentColor = MaterialTheme.colorScheme.onError,
    actionColor = MaterialTheme.colorScheme.onError,
  )
}