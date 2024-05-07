package com.kenkoro.taurus.client.feature.sewing.presentation.screen.login

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.kenkoro.taurus.client.core.connectivity.NetworkStatus
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.TokenDto
import com.kenkoro.taurus.client.feature.sewing.presentation.LoginResult
import com.kenkoro.taurus.client.feature.sewing.presentation.screen.login.components.LoginContent
import com.kenkoro.taurus.client.feature.sewing.presentation.shared.components.TaurusSnackbar
import com.kenkoro.taurus.client.ui.theme.AppTheme

@Composable
fun LoginScreen(
  subject: String,
  password: String,
  onSubject: (String) -> Unit,
  onPassword: (String) -> Unit,
  onLogin: suspend () -> Result<TokenDto>,
  onLoginResult: (LoginResult) -> Unit,
  onEncryptAll: (String) -> Unit,
  onNavigateToOrderScreen: () -> Unit,
  networkStatus: NetworkStatus,
) {
  val snackbarHostState = remember { SnackbarHostState() }
  val errorSnackbarHostState = remember { SnackbarHostState() }
  val internetSnackbarHostState = remember { SnackbarHostState() }


  AppTheme {
    Scaffold(
      snackbarHost = {
        TaurusSnackbar(
          snackbarHostState = snackbarHostState,
          onDismiss = { snackbarHostState.currentSnackbarData?.dismiss() },
        )

        TaurusSnackbar(
          snackbarHostState = errorSnackbarHostState,
          onDismiss = { errorSnackbarHostState.currentSnackbarData?.dismiss() },
          containerColor = MaterialTheme.colorScheme.errorContainer,
          contentColor = MaterialTheme.colorScheme.onErrorContainer,
        )

        TaurusSnackbar(
          snackbarHostState = internetSnackbarHostState,
          onDismiss = { internetSnackbarHostState.currentSnackbarData?.dismiss() },
          containerColor = MaterialTheme.colorScheme.errorContainer,
          contentColor = MaterialTheme.colorScheme.onErrorContainer,
          centeredContent = true,
        )
      },
      content = {
        Surface(
          modifier =
          Modifier
            .fillMaxSize()
            .padding(it),
        ) {
          LoginContent(
            subject = subject,
            password = password,
            onSubject = onSubject,
            onPassword = onPassword,
            onLogin = onLogin,
            onLoginResult = onLoginResult,
            onEncryptAll = onEncryptAll,
            onNavigateToOrderScreen = onNavigateToOrderScreen,
            networkStatus = networkStatus,
            snackbarHostState = snackbarHostState,
            errorSnackbarHostState = errorSnackbarHostState,
            internetSnackbarHostState = internetSnackbarHostState,
          )
        }
      }
    )
  }
}

@Preview
@Composable
private fun LoginScreenPrev() {
  AppTheme {
    LoginScreen(
      subject = "",
      password = "",
      onSubject = {},
      onPassword = {},
      onLogin = { Result.success(TokenDto("")) },
      onLoginResult = {},
      onEncryptAll = {},
      onNavigateToOrderScreen = { /*TODO*/ },
      networkStatus = NetworkStatus.Available,
    )
  }
}