package com.kenkoro.taurus.client.feature.login.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import com.kenkoro.taurus.client.core.connectivity.NetworkStatus
import com.kenkoro.taurus.client.core.local.LocalContentWidth
import com.kenkoro.taurus.client.feature.login.presentation.components.util.LoginTextFieldsExtras
import com.kenkoro.taurus.client.feature.login.presentation.util.LoginScreenNavigator
import com.kenkoro.taurus.client.feature.login.presentation.util.LoginScreenRemoteHandler
import com.kenkoro.taurus.client.feature.login.presentation.util.LoginScreenSnackbarsHolder
import com.kenkoro.taurus.client.feature.login.presentation.util.LoginScreenUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun LoginContent(
  modifier: Modifier = Modifier,
  utils: LoginScreenUtils,
  remoteHandler: LoginScreenRemoteHandler,
  navigator: LoginScreenNavigator,
  snackbarsHolder: LoginScreenSnackbarsHolder,
) {
  val contentWidth = LocalContentWidth.current
  val focusManager = LocalFocusManager.current

  val scope = rememberCoroutineScope()
  val interactionSource = remember { MutableInteractionSource() }
  var isAuthenticating by rememberSaveable {
    mutableStateOf(false)
  }

  val extras =
    LoginTextFieldsExtras(
      isAuthenticating = isAuthenticating,
      whenLoginSubmitted = { subject, password ->
        scope.launch(Dispatchers.IO) {
          isAuthenticating = true
          val result = remoteHandler.login(subject, password)
          isAuthenticating = false

          result.onSuccess {
            utils.encryptAllUserCredentials(subject, password, it.token)
            withContext(Dispatchers.Main) { navigator.toOrderScreen() }
          }

          result.onFailure { snackbarsHolder.loginError() }
        }
      },
    )

  Column(
    modifier =
      modifier
        .fillMaxSize()
        .clickable(interactionSource = interactionSource, indication = null) {
          focusManager.clearFocus()
        },
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center,
  ) {
    if (utils.network != NetworkStatus.Available) {
      LaunchedEffect(utils.network) { snackbarsHolder.internetConnectionError() }
    }

    Column(
      modifier = Modifier.fillMaxSize(),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      LoginTextFields(
        modifier = Modifier.width(contentWidth.standard),
        utils = utils,
        extras = extras,
      )
    }
  }
}