package com.kenkoro.taurus.client.feature.sewing.presentation.screen.login.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.kenkoro.taurus.client.core.connectivity.NetworkStatus
import com.kenkoro.taurus.client.core.local.LocalContentHeight
import com.kenkoro.taurus.client.core.local.LocalContentWidth
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.TokenDto

@Composable
fun LoginContent(
  subject: String,
  password: String,
  onSubject: (String) -> Unit,
  onPassword: (String) -> Unit,
  onLogin: suspend () -> Result<TokenDto>,
  onEncryptAll: (String) -> Unit,
  onNavigateToOrderScreen: () -> Unit,
  onInternetConnectionErrorShowSnackbar: suspend () -> SnackbarResult,
  onLoginErrorShowSnackbar: suspend () -> SnackbarResult,
  onInvalidLoginCredentialsShowSnackbar: suspend () -> SnackbarResult,
  onHelpTextClickShowSnackbar: suspend () -> SnackbarResult,
  networkStatus: NetworkStatus,
  modifier: Modifier = Modifier,
) {
  val contentWidth = LocalContentWidth.current
  val contentHeight = LocalContentHeight.current

  Column(
    modifier = modifier.fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center,
  ) {
    if (networkStatus != NetworkStatus.Available) {
      LaunchedEffect(networkStatus) { onInternetConnectionErrorShowSnackbar() }
    }

    Column(
      modifier =
        Modifier
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
        onNavigateToOrderScreen = onNavigateToOrderScreen,
        onEncryptAll = onEncryptAll,
        onLoginErrorShowSnackbar = onLoginErrorShowSnackbar,
        onInvalidLoginCredentialsShowSnackbar = onInvalidLoginCredentialsShowSnackbar,
        networkStatus = networkStatus,
        modifier = Modifier.width(contentWidth.standard),
      )
    }
    Column(
      modifier = Modifier.fillMaxSize(),
      verticalArrangement = Arrangement.Bottom,
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      HelpContent(onHelpTextClickShowSnackbar = onHelpTextClickShowSnackbar)
      Spacer(modifier = Modifier.height(contentHeight.halfStandard))
    }
  }
}