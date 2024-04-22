package com.kenkoro.taurus.client.feature.sewing.presentation.shared.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.kenkoro.taurus.client.core.local.LocalPadding
import com.kenkoro.taurus.client.core.local.LocalShape

@Composable
fun TaurusSnackbar(
  snackbarHostState: SnackbarHostState,
  modifier: Modifier = Modifier,
  onDismiss: () -> Unit = {},
  containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
  contentColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
  centeredContent: Boolean = false,
) {
  val shape = LocalShape.current
  val padding = LocalPadding.current

  SnackbarHost(
    hostState = snackbarHostState,
    snackbar = { data ->
      Snackbar(
        modifier =
          Modifier
            .padding(padding.snackbar),
        content = {
          if (centeredContent) {
            Text(
              modifier = Modifier.fillMaxWidth(),
              text = data.visuals.message,
              style = MaterialTheme.typography.bodyMedium,
              textAlign = TextAlign.Center,
            )
          } else {
            Text(
              text = data.visuals.message,
              style = MaterialTheme.typography.bodyMedium,
            )
          }
        },
        shape = RoundedCornerShape(shape.medium),
        containerColor = containerColor,
        contentColor = contentColor,
        action = {
          data.visuals.actionLabel?.let {
            TextButton(onClick = onDismiss) {
              Text(
                text = it,
                color = contentColor,
              )
            }
          }
        },
      )
    },
    modifier =
      modifier
        .fillMaxSize()
        .wrapContentHeight(Alignment.Bottom),
  )
}