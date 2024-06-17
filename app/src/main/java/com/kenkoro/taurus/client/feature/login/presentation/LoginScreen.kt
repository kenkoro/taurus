package com.kenkoro.taurus.client.feature.login.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.kenkoro.taurus.client.R
import com.kenkoro.taurus.client.core.connectivity.NetworkStatus
import com.kenkoro.taurus.client.feature.login.presentation.components.LoginContent
import com.kenkoro.taurus.client.feature.login.presentation.util.PasswordState
import com.kenkoro.taurus.client.feature.login.presentation.util.SubjectState
import com.kenkoro.taurus.client.feature.shared.components.TaurusSnackbar
import com.kenkoro.taurus.client.feature.shared.data.remote.dto.TokenDto
import com.kenkoro.taurus.client.feature.shared.states.TaurusTextFieldState
import com.kenkoro.taurus.client.ui.theme.AppTheme
import kotlinx.coroutines.delay

@Composable
fun LoginScreen(
  modifier: Modifier = Modifier,
  subject: SubjectState,
  password: PasswordState,
  networkStatus: NetworkStatus,
  onSetErrorMessages: (TaurusTextFieldState, String, String) -> Unit,
  onLogin: suspend (subject: String, password: String) -> Result<TokenDto>,
  onEncryptAll: (String, String, String) -> Unit,
  onExit: () -> Unit = {},
  onShowErrorTitle: () -> Boolean = { false },
  onNavigateToOrderScreen: () -> Unit,
) {
  val snackbarHostState = remember { SnackbarHostState() }
  val errorSnackbarHostState = remember { SnackbarHostState() }
  val internetSnackbarHostState = remember { SnackbarHostState() }

  val internetConnectionErrorMessage = stringResource(id = R.string.check_internet_connection)
  val requestErrorMessage = stringResource(id = R.string.request_error)

  val okActionLabel = stringResource(id = R.string.ok)

  var visible by rememberSaveable {
    mutableStateOf(false)
  }

  LaunchedEffect(Unit) {
    delay(100L)
    visible = true
  }

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
            modifier
              .fillMaxSize()
              .padding(it),
        ) {
          AnimatedVisibility(visible = visible) {
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
              onShowErrorTitle = onShowErrorTitle,
            )
          }
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