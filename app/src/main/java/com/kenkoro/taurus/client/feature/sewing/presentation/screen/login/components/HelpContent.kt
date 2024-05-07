package com.kenkoro.taurus.client.feature.sewing.presentation.screen.login.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.kenkoro.taurus.client.R
import kotlinx.coroutines.launch

@Composable
fun HelpContent(
  snackbarHostState: SnackbarHostState,
  modifier: Modifier = Modifier,
) {
  val scope = rememberCoroutineScope()

  val notImplementedYetMessage = stringResource(id = R.string.login_forgot_password_not_implemented)
  val okActionLabel = stringResource(id = R.string.ok)

  Column(
    modifier =
    modifier
      .wrapContentSize()
      .clickable {
        scope.launch {
          snackbarHostState.showSnackbar(
            message = notImplementedYetMessage,
            actionLabel = okActionLabel,
          )
        }
      },
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Text(
      text = stringResource(id = R.string.login_forgot_password),
      textAlign = TextAlign.Center,
      style = MaterialTheme.typography.labelSmall,
    )
  }
}