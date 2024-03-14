package com.kenkoro.taurus.client.feature.sewing.presentation.dashboard.screen

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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kenkoro.taurus.client.R
import com.kenkoro.taurus.client.feature.sewing.presentation.dashboard.screen.components.BottomBarHost
import com.kenkoro.taurus.client.feature.sewing.presentation.login.screen.LoginViewModel
import com.kenkoro.taurus.client.feature.sewing.presentation.order.screen.OrderScreen
import com.kenkoro.taurus.client.feature.sewing.presentation.shared.components.ErrorSnackbar
import com.kenkoro.taurus.client.feature.sewing.presentation.shared.components.showErrorSnackbar
import com.kenkoro.taurus.client.feature.sewing.presentation.shared.handlers.LoginResponseHandler
import com.kenkoro.taurus.client.feature.sewing.presentation.shared.handlers.ResponseHandler
import com.kenkoro.taurus.client.feature.sewing.presentation.user.screen.UserScreen
import com.kenkoro.taurus.client.feature.sewing.presentation.util.DecryptedCredentials
import com.kenkoro.taurus.client.feature.sewing.presentation.util.LocalCredentials
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
  val handler: ResponseHandler = LoginResponseHandler()

  val locallyStoredSubject =
    DecryptedCredentials.getDecryptedCredential(
      filename = LocalCredentials.SUBJECT_FILENAME,
      context = context,
    ).value
  val locallyStoredPassword =
    DecryptedCredentials.getDecryptedCredential(
      filename = LocalCredentials.PASSWORD_FILENAME,
      context = context,
    ).value

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
        LaunchedEffect(Unit) {
          if (locallyStoredSubject.isNotBlank() && locallyStoredPassword.isNotBlank()) {
            handler.handle(
              subject = locallyStoredSubject,
              password = locallyStoredPassword,
              context = context,
              loginViewModel = loginViewModel,
            ).run {
              dashboardViewModel.onResponse(this)
            }
          } else {
            onDashboardNavigate()
          }
        }

        when (dashboardViewModel.loginResponseType.value) {
          LoginResponseType.Success -> {
            BottomBarHost { index ->
              when (index) {
                BottomBarHostIndices.ORDER_SCREEN -> OrderScreen()
                else -> UserScreen()
              }
            }
          }

          LoginResponseType.RequestFailure -> {
            showErrorSnackbar(
              snackbarHostState = snackbarHostState,
              key = dashboardViewModel.loginResponseType.value,
              message = message,
              onActionPerformed = {
                if (locallyStoredSubject.isNotBlank() && locallyStoredPassword.isNotBlank()) {
                  handler.handle(
                    subject = locallyStoredSubject,
                    password = locallyStoredPassword,
                    context = context,
                    loginViewModel,
                  ).run {
                    dashboardViewModel.onResponse(this)
                  }
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