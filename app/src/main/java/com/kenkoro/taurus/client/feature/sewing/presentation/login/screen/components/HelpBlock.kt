package com.kenkoro.taurus.client.feature.sewing.presentation.login.screen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kenkoro.taurus.client.R

@Composable
fun HelpBlock(modifier: Modifier = Modifier) {
  Column(
    modifier =
      modifier
        .wrapContentSize()
        .clickable {},
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Spacer(modifier = Modifier.height(5.dp))
    Text(
      text = stringResource(id = R.string.login_forgot_password_top),
      textAlign = TextAlign.Center,
      style = MaterialTheme.typography.labelSmall,
    )
    Text(
      text = stringResource(id = R.string.login_forgot_password_bottom),
      textAlign = TextAlign.Center,
      style = MaterialTheme.typography.labelSmall,
    )
    Spacer(modifier = Modifier.height(10.dp))
  }
}