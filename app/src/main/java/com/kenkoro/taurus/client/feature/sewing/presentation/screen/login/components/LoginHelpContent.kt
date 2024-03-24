package com.kenkoro.taurus.client.feature.sewing.presentation.screen.login.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
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
import com.kenkoro.taurus.client.core.local.LocalContentHeight
import kotlinx.coroutines.launch

@Composable
fun LoginHelpContent(
  modifier: Modifier = Modifier,
  snackbarHostState: SnackbarHostState,
) {
  val scope = rememberCoroutineScope()
  val contentHeight = LocalContentHeight.current

  val message = stringResource(id = R.string.login_forgot_password_not_implemented)
  Column(
    modifier =
      modifier
        .wrapContentSize()
        .clickable {
          scope.launch {
            snackbarHostState.showSnackbar(
              message = message,
              withDismissAction = true,
            )
          }
        },
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Spacer(modifier = Modifier.height(contentHeight.small))
    Text(
      text = stringResource(id = R.string.login_forgot_password),
      textAlign = TextAlign.Center,
      style = MaterialTheme.typography.labelSmall,
    )
  }
}