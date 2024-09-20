package com.kenkoro.taurus.client.feature.auth.presentation

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
import androidx.hilt.navigation.compose.hiltViewModel
import com.kenkoro.taurus.client.R
import com.kenkoro.taurus.client.feature.auth.presentation.components.AuthContent
import com.kenkoro.taurus.client.feature.auth.presentation.util.AuthScreenNavigator
import com.kenkoro.taurus.client.feature.auth.presentation.util.AuthScreenShared
import com.kenkoro.taurus.client.feature.auth.presentation.util.AuthScreenSnackbarsHolder
import com.kenkoro.taurus.client.feature.auth.presentation.util.AuthScreenUtils
import com.kenkoro.taurus.client.feature.shared.components.TaurusSnackbar
import com.kenkoro.taurus.client.ui.theme.AppTheme
import kotlinx.coroutines.delay

@Composable
fun AuthScreen(
  modifier: Modifier = Modifier,
  navigator: AuthScreenNavigator,
  shared: AuthScreenShared,
) {
  val viewModel: AuthViewModel = hiltViewModel()

  val snackbarHostState = remember { SnackbarHostState() }
  val errorSnackbarHostState = remember { SnackbarHostState() }
  val internetSnackbarHostState = remember { SnackbarHostState() }
  var isAuthContentVisible by rememberSaveable {
    mutableStateOf(false)
  }

  val internetConnectionErrorMessage = stringResource(id = R.string.check_internet_connection)
  val requestErrorMessage = stringResource(id = R.string.request_error)

  val okActionLabel = stringResource(id = R.string.ok)

  val snackbarsHolder =
    AuthScreenSnackbarsHolder(
      internetConnectionError = {
        internetSnackbarHostState.showSnackbar(
          message = internetConnectionErrorMessage,
          duration = SnackbarDuration.Indefinite,
        )
      },
      loginError = {
        errorSnackbarHostState.showSnackbar(
          message = requestErrorMessage,
          actionLabel = okActionLabel,
        )
      },
    )
  val utils =
    AuthScreenUtils(
      subject = viewModel.subject,
      password = viewModel.password,
      showErrorTitle = viewModel::showErrorTitle,
      auth = viewModel::auth,
      encryptAll = viewModel::encryptAll,
    )

  LaunchedEffect(Unit) {
    delay(100L)
    isAuthContentVisible = true
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
          AnimatedVisibility(visible = isAuthContentVisible) {
            AuthContent(
              navigator = navigator,
              shared = shared,
              snackbarsHolder = snackbarsHolder,
              utils = utils,
            )
          }
        }
      },
    )
  }
}