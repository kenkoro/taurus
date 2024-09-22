package com.kenkoro.taurus.client.feature.auth.presentation.components

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
import com.kenkoro.taurus.client.feature.auth.presentation.components.util.AuthExtras
import com.kenkoro.taurus.client.feature.auth.presentation.util.AuthScreenNavigator
import com.kenkoro.taurus.client.feature.auth.presentation.util.AuthScreenShared
import com.kenkoro.taurus.client.feature.auth.presentation.util.AuthScreenSnackbarsHolder
import com.kenkoro.taurus.client.feature.auth.presentation.util.AuthScreenUtils
import com.kenkoro.taurus.client.feature.shared.states.AuthStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun AuthContent(
  modifier: Modifier = Modifier,
  navigator: AuthScreenNavigator,
  shared: AuthScreenShared,
  snackbarsHolder: AuthScreenSnackbarsHolder,
  utils: AuthScreenUtils,
) {
  val contentWidth = LocalContentWidth.current
  val focusManager = LocalFocusManager.current

  val scope = rememberCoroutineScope()
  val interactionSource = remember { MutableInteractionSource() }
  var isAuthenticating by rememberSaveable {
    mutableStateOf(false)
  }

  val extras =
    AuthExtras(
      isAuthenticating = isAuthenticating,
      whenLoginSubmitted = { subject, password ->
        scope.launch(Dispatchers.IO) {
          isAuthenticating = true
          val authRequest = utils.auth(subject, password)
          isAuthenticating = false

          authRequest.onSuccess {
            utils.encryptAll(subject, password, it.token)
            shared.proceedAuth(AuthStatus.Success)
            withContext(Dispatchers.Main) { navigator.toOrderScreen() }
          }

          authRequest.onFailure {
            shared.proceedAuth(AuthStatus.Failure)
            snackbarsHolder.loginError()
          }
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
    if (shared.network != NetworkStatus.Available) {
      LaunchedEffect(shared.network) { snackbarsHolder.internetConnectionError() }
    }

    Column(
      modifier = Modifier.fillMaxSize(),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      AuthTextFields(
        modifier = Modifier.width(contentWidth.standard),
        shared = shared,
        extras = extras,
        utils = utils,
      )
    }
  }
}