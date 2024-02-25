package com.kenkoro.taurus.client.feature.sewing.presentation.login.screen.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kenkoro.taurus.client.R

@Composable
fun LoginBlock(
  onLogin: () -> Unit,
  snackbarHostState: SnackbarHostState,
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
    LoginFieldBlock(onLogin = onLogin, snackbarHostState = snackbarHostState)
  }
}