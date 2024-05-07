package com.kenkoro.taurus.client.feature.sewing.presentation.screen.login.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.kenkoro.taurus.client.R
import com.kenkoro.taurus.client.core.connectivity.NetworkStatus
import com.kenkoro.taurus.client.core.local.LocalContentHeight
import com.kenkoro.taurus.client.core.local.LocalContentWidth
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.TokenDto
import com.kenkoro.taurus.client.feature.sewing.presentation.LoginResult
import kotlinx.coroutines.launch

@Composable
fun LoginContent(
  subject: String,
  password: String,
  onSubject: (String) -> Unit,
  onPassword: (String) -> Unit,
  onLogin: suspend () -> Result<TokenDto>,
  onLoginResult: (LoginResult) -> Unit,
  onEncryptAll: (String) -> Unit,
  onNavigateToOrderScreen: () -> Unit,
  networkStatus: NetworkStatus,
  snackbarHostState: SnackbarHostState,
  errorSnackbarHostState: SnackbarHostState,
  internetSnackbarHostState: SnackbarHostState,
  modifier: Modifier = Modifier,
) {
  val contentWidth = LocalContentWidth.current
  val contentHeight = LocalContentHeight.current

  val internetConnectionErrorMessage = stringResource(id = R.string.check_internet_connection)

  Column(
    modifier = modifier.fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center,
  ) {
    if (networkStatus != NetworkStatus.Available) {
      LaunchedEffect(networkStatus) {
        launch {
          internetSnackbarHostState.showSnackbar(
            message = internetConnectionErrorMessage,
            duration = SnackbarDuration.Indefinite,
          )
        }
      }
    }

    Column(
      modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(.6F),
      verticalArrangement = Arrangement.Bottom,
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      FieldsContent(
        subject = subject,
        password = password,
        onSubject = onSubject,
        onPassword = onPassword,
        onLogin = onLogin,
        onLoginResult = onLoginResult,
        onNavigateToOrderScreen = onNavigateToOrderScreen,
        onEncryptAll = onEncryptAll,
        errorSnackbarHostState = errorSnackbarHostState,
        networkStatus = networkStatus,
        modifier = Modifier.width(contentWidth.orderItem),
      )
    }
    Column(
      modifier = Modifier.fillMaxSize(),
      verticalArrangement = Arrangement.Bottom,
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      HelpContent(snackbarHostState = snackbarHostState)
      Spacer(modifier = Modifier.height(contentHeight.halfStandard))
    }
  }
}