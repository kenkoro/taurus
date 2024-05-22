package com.kenkoro.taurus.client.feature.sewing.presentation.screen.login

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.kenkoro.taurus.client.R
import com.kenkoro.taurus.client.core.connectivity.NetworkStatus
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.TokenDto
import com.kenkoro.taurus.client.feature.sewing.presentation.screen.login.components.LoginContent
import com.kenkoro.taurus.client.feature.sewing.presentation.screen.login.util.PasswordState
import com.kenkoro.taurus.client.feature.sewing.presentation.screen.login.util.SubjectState
import com.kenkoro.taurus.client.feature.sewing.presentation.shared.TaurusTextFieldState
import com.kenkoro.taurus.client.feature.sewing.presentation.shared.components.TaurusSnackbar
import com.kenkoro.taurus.client.ui.theme.AppTheme

@Composable
fun LoginScreen(
  networkStatus: NetworkStatus,
  subject: SubjectState,
  password: PasswordState,
  onSetErrorMessages: (TaurusTextFieldState, String, String) -> Unit,
  onLogin: suspend (subject: String, password: String) -> Result<TokenDto>,
  onEncryptAll: (String, String, String) -> Unit,
  onNavigateToOrderScreen: () -> Unit,
  onExit: () -> Unit = {},
) {
  val snackbarHostState = remember { SnackbarHostState() }
  val errorSnackbarHostState = remember { SnackbarHostState() }
  val internetSnackbarHostState = remember { SnackbarHostState() }

  val internetConnectionErrorMessage = stringResource(id = R.string.check_internet_connection)
  val requestErrorMessage = stringResource(id = R.string.request_error)

  val okActionLabel = stringResource(id = R.string.ok)

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
            networkStatus = networkStatus,
            subject = subject,
            password = password,
            onSetErrorMessages = onSetErrorMessages,
            onLogin = onLogin,
            onEncryptAll = onEncryptAll,
            onNavigateToOrderScreen = onNavigateToOrderScreen,
            onExit = onExit,
            onInternetConnectionErrorShowSnackbar = {
              internetSnackbarHostState.showSnackbar(
                message = internetConnectionErrorMessage,
                duration = SnackbarDuration.Indefinite,
              )
            },
            onLoginErrorShowSnackbar = {
              errorSnackbarHostState.showSnackbar(
                message = requestErrorMessage,
                actionLabel = okActionLabel,
              )
            },
          )
        }
      },
    )
  }
}

@Preview
@Composable
private fun LoginScreenPrev() {
  AppTheme {
    LoginScreen(
      networkStatus = NetworkStatus.Available,
      subject = SubjectState(),
      password = PasswordState(),
      onSetErrorMessages = { _, _, _ -> },
      onLogin = { _, _ -> Result.success(TokenDto("")) },
      onEncryptAll = { _, _, _ -> },
      onNavigateToOrderScreen = {},
    )
  }
}