package com.kenkoro.taurus.client.feature.login.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.kenkoro.taurus.client.core.connectivity.NetworkStatus
import com.kenkoro.taurus.client.feature.login.presentation.util.LoginScreenNavigator
import com.kenkoro.taurus.client.feature.login.presentation.util.LoginScreenRemoteHandler
import com.kenkoro.taurus.client.feature.login.presentation.util.LoginScreenUtils

@Composable
fun LoginRoute(
  modifier: Modifier = Modifier,
  navigator: LoginScreenNavigator,
  network: NetworkStatus = NetworkStatus.Unavailable,
  onExit: () -> Unit = {},
) {
  val viewModel: LoginViewModel = hiltViewModel()

  val remoteHandler = LoginScreenRemoteHandler(viewModel::login)
  val utils =
    LoginScreenUtils(
      subject = viewModel.subject,
      password = viewModel.password,
      network = network,
      encryptAllUserCredentials = viewModel::encryptAll,
      exit = onExit,
      showErrorTitle = viewModel::showErrorTitle,
    )

  LoginScreen(
    remoteHandler = remoteHandler,
    navigator = navigator,
    utils = utils,
  )
}