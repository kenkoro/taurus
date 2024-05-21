package com.kenkoro.taurus.client.feature.sewing.presentation.screen.login.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import com.kenkoro.taurus.client.R
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
  networkStatus: NetworkStatus,
  modifier: Modifier = Modifier,
) {
  val contentWidth = LocalContentWidth.current
  val contentHeight = LocalContentHeight.current
  val focusManager = LocalFocusManager.current

  val interactionSource = remember { MutableInteractionSource() }

  Column(
    modifier =
      modifier
        .fillMaxSize()
        .clickable(
          interactionSource = interactionSource,
          indication = null,
        ) {
          focusManager.clearFocus()
        },
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center,
  ) {
    if (networkStatus != NetworkStatus.Available) {
      LaunchedEffect(networkStatus) { onInternetConnectionErrorShowSnackbar() }
    }

    Column(
      modifier = Modifier.fillMaxSize(),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      Text(
        text = stringResource(id = R.string.login_screen_title),
        style = MaterialTheme.typography.titleLarge,
      )
      Spacer(modifier = Modifier.height(contentHeight.large))
      LoginTextFields(
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
  }
}