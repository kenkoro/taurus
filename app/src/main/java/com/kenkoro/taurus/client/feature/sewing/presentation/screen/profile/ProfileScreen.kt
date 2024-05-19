package com.kenkoro.taurus.client.feature.sewing.presentation.screen.profile

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
import com.kenkoro.taurus.client.feature.sewing.presentation.screen.util.LoginResult

@Composable
fun ProfileScreen(
  onDeleteAllCredentials: () -> Boolean,
  onNavigateToLoginScreen: () -> Unit,
  onLoginResult: (LoginResult) -> Unit,
) {
  Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
    Button(
      onClick = {
        val result = onDeleteAllCredentials()
        onLoginResult(LoginResult.NotLoggedYet)
        onNavigateToLoginScreen()
      },
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