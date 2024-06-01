package com.kenkoro.taurus.client.feature.profile.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.kenkoro.taurus.client.R

@Composable
fun ProfileScreen(
  onDeleteAllCredentials: () -> Boolean = { false },
  onNavigateToLoginScreen: () -> Unit = {},
  onResetLoginState: () -> Unit = {},
  onRestart: () -> Unit = {},
) {
  val onClick = {
    onDeleteAllCredentials()
    onResetLoginState()
    onNavigateToLoginScreen()
    onRestart()
  }

  Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
    Button(
      onClick = onClick,
      colors =
        ButtonDefaults.buttonColors(
          contentColor = MaterialTheme.colorScheme.onErrorContainer,
          containerColor = MaterialTheme.colorScheme.errorContainer,
        ),
    ) {
      Text(text = stringResource(id = R.string.quit_from_account))
    }
  }
}