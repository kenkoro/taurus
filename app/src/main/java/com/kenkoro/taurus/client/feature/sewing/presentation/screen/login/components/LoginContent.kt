package com.kenkoro.taurus.client.feature.sewing.presentation.screen.login.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import com.kenkoro.taurus.client.R
import com.kenkoro.taurus.client.core.connectivity.NetworkStatus
import com.kenkoro.taurus.client.core.local.LocalContentWidth
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.TokenDto
import com.kenkoro.taurus.client.feature.sewing.presentation.screen.login.util.PasswordState
import com.kenkoro.taurus.client.feature.sewing.presentation.screen.login.util.SubjectState
import com.kenkoro.taurus.client.feature.sewing.presentation.shared.TaurusTextFieldState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun LoginContent(
  modifier: Modifier = Modifier,
  networkStatus: NetworkStatus,
  subject: SubjectState,
  password: PasswordState,
  onSetErrorMessages: (TaurusTextFieldState, String, String) -> Unit,
  onLogin: suspend (subject: String, password: String) -> Result<TokenDto>,
  onEncryptAll: (String, String, String) -> Unit,
  onNavigateToOrderScreen: () -> Unit,
  onExit: () -> Unit = {},
  onInternetConnectionErrorShowSnackbar: suspend () -> SnackbarResult,
  onLoginErrorShowSnackbar: suspend () -> SnackbarResult,
) {
  val contentWidth = LocalContentWidth.current
  val contentHeight = LocalContentWidth.current
  val focusManager = LocalFocusManager.current

  val scope = rememberCoroutineScope()
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
      modifier = Modifier.weight(1F),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      LoginTextFields(
        modifier = Modifier.width(contentWidth.standard),
        networkStatus = networkStatus,
        subject = subject,
        password = password,
        onSetErrorMessages = onSetErrorMessages,
        onLoginSubmitted = { subject, password ->
          scope.launch(Dispatchers.IO) {
            val result = onLogin(subject, password)
            result.onSuccess {
              onEncryptAll(subject, password, it.token)
              withContext(Dispatchers.Main) { onNavigateToOrderScreen() }
            }

            result.onFailure { onLoginErrorShowSnackbar() }
          }
        },
        onExit = onExit,
      )
    }
    Box(
      modifier = Modifier.height(contentHeight.halfStandard),
      contentAlignment = Alignment.BottomCenter,
    ) {
      val logoId =
        if (isSystemInDarkTheme()) {
          R.drawable.ic_splashscreen_night_foreground
        } else {
          R.drawable.ic_splashscreen_foreground
        }
      Image(
        painter = painterResource(id = logoId),
        contentDescription = "TaurusLogoOnLoginForm",
      )
    }
  }
}