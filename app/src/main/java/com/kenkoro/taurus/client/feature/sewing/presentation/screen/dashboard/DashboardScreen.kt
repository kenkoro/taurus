package com.kenkoro.taurus.client.feature.sewing.presentation.screen.dashboard

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kenkoro.taurus.client.R
import com.kenkoro.taurus.client.core.connectivity.ConnectivityObserver
import com.kenkoro.taurus.client.core.connectivity.NetworkConnectivityObserver
import com.kenkoro.taurus.client.core.connectivity.Status
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.request.LoginRequest
import com.kenkoro.taurus.client.feature.sewing.presentation.screen.dashboard.components.BottomBarHost
import com.kenkoro.taurus.client.feature.sewing.presentation.screen.login.LoginViewModel
import com.kenkoro.taurus.client.feature.sewing.presentation.screen.order.OrderScreen
import com.kenkoro.taurus.client.feature.sewing.presentation.screen.user.UserScreen
import com.kenkoro.taurus.client.feature.sewing.presentation.shared.components.ErrorSnackbar
import com.kenkoro.taurus.client.feature.sewing.presentation.shared.components.showErrorSnackbar
import com.kenkoro.taurus.client.feature.sewing.presentation.shared.handlers.handleLogin
import com.kenkoro.taurus.client.feature.sewing.presentation.util.LoginResponseType
import com.kenkoro.taurus.client.ui.theme.AppTheme

object BottomBarHostIndices {
  const val ORDER_SCREEN = 1
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DashboardScreen(
  onDashboardNavigate: () -> Unit = {},
  loginViewModel: LoginViewModel = hiltViewModel(),
  dashboardViewModel: DashboardViewModel = hiltViewModel(),
) {
  val snackbarHostState = remember { SnackbarHostState() }

  val message = stringResource(id = R.string.request_error)
  val context = LocalContext.current

  val networkConnectivityObserver: ConnectivityObserver = NetworkConnectivityObserver(context)

  AppTheme {
    Scaffold(
      snackbarHost = {
        SnackbarHost(hostState = snackbarHostState) {
          ErrorSnackbar(
            modifier = Modifier.padding(bottom = 20.dp),
            snackbarData = it,
          )
        }
      },
    ) {
      Surface(
        modifier =
          Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
      ) {
        val networkStatus by networkConnectivityObserver
          .observer()
          .collectAsState(initial = Status.Unavailable)
        if (networkStatus != Status.Available) {
          showErrorSnackbar(
            snackbarHostState = snackbarHostState,
            key = networkStatus,
            message = stringResource(id = R.string.check_internet_connection),
            actionLabel = null,
          )
        } else {
          LaunchedEffect(Unit) {
            dashboardViewModel.onResponse(LoginResponseType.Pending)
            handleLogin(
              login = { subject, password, encryptSubjectAndPassword ->
                loginViewModel.loginAndEncryptCredentials(
                  request = LoginRequest(subject, password),
                  context = context,
                  encryptSubjectAndPassword = encryptSubjectAndPassword,
                )
              },
              context = context,
            ).run {
              dashboardViewModel.onResponse(this)
            }
          }
        }

        LaunchedEffect(Unit) {
          handleLogin(
            login = { subject, password, encryptSubjectAndPassword ->
              loginViewModel.loginAndEncryptCredentials(
                request = LoginRequest(subject, password),
                context = context,
                encryptSubjectAndPassword = encryptSubjectAndPassword,
              )
            },
            context = context,
          ).run {
            dashboardViewModel.onResponse(this)
          }
        }

        when (dashboardViewModel.loginResponseType.value) {
          LoginResponseType.Success -> {
            BottomBarHost { index ->
              when (index) {
                BottomBarHostIndices.ORDER_SCREEN -> OrderScreen()
                else ->
                  UserScreen(
                    networkStatus = networkStatus,
                  )
              }
            }
          }

          LoginResponseType.RequestFailure -> {
            showErrorSnackbar(
              snackbarHostState = snackbarHostState,
              key = dashboardViewModel.loginResponseType.value,
              message = message,
              onActionPerformed = {
                dashboardViewModel.onResponse(LoginResponseType.Pending)
                handleLogin(
                  login = { subject, password, encryptSubjectAndPassword ->
                    loginViewModel.loginAndEncryptCredentials(
                      request = LoginRequest(subject, password),
                      context = context,
                      encryptSubjectAndPassword = encryptSubjectAndPassword,
                    )
                  },
                  context = context,
                ).run {
                  dashboardViewModel.onResponse(this)
                }
              },
            )
          }

          LoginResponseType.Failure -> {
            onDashboardNavigate()
          }

          LoginResponseType.BadCredentials -> {
            onDashboardNavigate()
          }

          LoginResponseType.Pending -> {
            Column(
              modifier = Modifier.fillMaxSize(),
              horizontalAlignment = Alignment.CenterHorizontally,
              verticalArrangement = Arrangement.Center,
            ) {
              CircularProgressIndicator()
            }
          }
        }
      }
    }
  }
}

@Preview
@Composable
private fun DashboardScreenPrev() {
  DashboardScreen()
}