package com.kenkoro.taurus.client.feature.login.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.kenkoro.taurus.client.R
import kotlinx.coroutines.launch

@Deprecated("Not in use anymore")
@Composable
fun HelpContent(
  onHelpTextClickShowSnackbar: suspend () -> SnackbarResult,
  modifier: Modifier = Modifier,
) {
  val scope = rememberCoroutineScope()

  Column(
    modifier =
      modifier
        .wrapContentSize()
        .clickable {
          scope.launch { onHelpTextClickShowSnackbar() }
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