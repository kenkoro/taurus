package com.kenkoro.taurus.client.feature.login.presentation.login.screen.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kenkoro.taurus.client.R
import com.kenkoro.taurus.client.ui.theme.AppTheme

@Composable
fun LoginBlock(
  onLogin: () -> Unit,
  @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
) {
  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.Center,
  ) {
    Text(
      text = stringResource(id = R.string.login_credentials_label),
      style = MaterialTheme.typography.headlineLarge,
    )
    Spacer(modifier = Modifier.height(30.dp))
    LoginFieldBlock(onLogin = onLogin)
  }
}

@Preview(showBackground = true)
@Composable
fun LoginBlockPreview() {
  AppTheme {
    LoginBlock(onLogin = {})
  }
}