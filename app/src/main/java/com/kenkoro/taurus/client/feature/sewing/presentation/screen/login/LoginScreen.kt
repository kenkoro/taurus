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
import com.kenkoro.taurus.client.feature.sewing.presentation.shared.components.TaurusSnackbar
import com.kenkoro.taurus.client.feature.sewing.presentation.viewmodels.LoginResult
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

  val internetConnectionErrorMessage = stringResource(id = R.string.check_internet_connection)
  val requestErrorMessage = stringResource(id = R.string.request_error)
  val subjectAndPasswordCannotBeBlankMessage =
    stringResource(id = R.string.subject_and_password_cannot_be_blank)
  val notImplementedYetMessage = stringResource(id = R.string.login_forgot_password_not_implemented)

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
            subject = subject,
            password = password,
            onSubject = onSubject,
            onPassword = onPassword,
            onLogin = onLogin,
            onLoginResult = onLoginResult,
            onEncryptAll = onEncryptAll,
            onNavigateToOrderScreen = onNavigateToOrderScreen,
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
            onInvalidLoginCredentialsShowSnackbar = {
              errorSnackbarHostState.showSnackbar(
                message = subjectAndPasswordCannotBeBlankMessage,
                actionLabel = okActionLabel,
              )
            },
            onHelpTextClickShowSnackbar = {
              snackbarHostState.showSnackbar(
                message = notImplementedYetMessage,
                actionLabel = okActionLabel,
              )
            },
            networkStatus = networkStatus,
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