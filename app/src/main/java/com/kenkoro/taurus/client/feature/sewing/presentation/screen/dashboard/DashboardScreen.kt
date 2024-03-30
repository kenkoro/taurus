package com.kenkoro.taurus.client.feature.sewing.presentation.screen.dashboard

import android.annotation.SuppressLint
import android.util.Log
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kenkoro.taurus.client.R
import com.kenkoro.taurus.client.core.connectivity.ConnectivityObserver
import com.kenkoro.taurus.client.core.connectivity.NetworkConnectivityObserver
import com.kenkoro.taurus.client.core.connectivity.Status
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.request.LoginRequestDto
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.response.GetUserResponseDto
import com.kenkoro.taurus.client.feature.sewing.presentation.screen.dashboard.components.BottomBarHost
import com.kenkoro.taurus.client.feature.sewing.presentation.screen.login.LoginViewModel
import com.kenkoro.taurus.client.feature.sewing.presentation.screen.order.OrderScreen
import com.kenkoro.taurus.client.feature.sewing.presentation.screen.user.UserScreen
import com.kenkoro.taurus.client.feature.sewing.presentation.screen.user.UserViewModel
import com.kenkoro.taurus.client.feature.sewing.presentation.shared.components.ErrorSnackbar
import com.kenkoro.taurus.client.feature.sewing.presentation.shared.components.showErrorSnackbar
import com.kenkoro.taurus.client.feature.sewing.presentation.shared.handlers.handleLoginWithLocallyScopedCredentials
import com.kenkoro.taurus.client.feature.sewing.presentation.shared.handlers.handleUserGetWithLocallyScopedCredentials
import com.kenkoro.taurus.client.feature.sewing.presentation.util.LoginResponseType
import com.kenkoro.taurus.client.ui.theme.AppTheme
import io.ktor.client.call.body

object BottomBarHostIndices {
  const val USER_SCREEN = 1
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DashboardScreen(
  onDashboardNavigate: () -> Unit = {},
  loginViewModel: LoginViewModel = hiltViewModel(),
  // dashboardViewModel: DashboardViewModel = hiltViewModel(),
  userViewModel: UserViewModel = hiltViewModel(),
) {
  val context = LocalContext.current

  val snackbarHostState = remember { SnackbarHostState() }
  var loginResponseType by remember {
    mutableStateOf(LoginResponseType.Pending)
  }
  val networkConnectivityObserver: ConnectivityObserver = NetworkConnectivityObserver(context)
  val networkStatus by networkConnectivityObserver
    .observer()
    .collectAsState(initial = Status.Unavailable)

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
        if (networkStatus != Status.Available) {
          loginResponseType = LoginResponseType.RequestFailure
          showErrorSnackbar(
            snackbarHostState = snackbarHostState,
            key = networkStatus,
            message = stringResource(id = R.string.check_internet_connection),
            actionLabel = null,
          )
        } else {
          LaunchedEffect(Unit) {
            loginResponseType = LoginResponseType.Pending
            loginResponseType =
              handleLoginWithLocallyScopedCredentials(
                login = { subject, password, encryptSubjectAndPassword ->
                  loginViewModel.loginAndEncryptCredentials(
                    request = LoginRequestDto(subject, password),
                    context = context,
                    encryptSubjectAndPassword = encryptSubjectAndPassword,
                  )
                },
                context = context,
              )

            handleUserGetWithLocallyScopedCredentials(context = context) { firstName, token ->
              try {
                userViewModel
                  .getUser(firstName, token)
                  .body<GetUserResponseDto>().run {
                    userViewModel.onGetUserResponseDto(this)
                  }
              } catch (e: Exception) {
                Log.d("kenkoro", e.message!!)
              }
            }
          }
        }
      }

      when (loginResponseType) {
        LoginResponseType.Success -> {
          BottomBarHost { index ->
            when (index) {
              BottomBarHostIndices.USER_SCREEN -> {
                UserScreen(
                  user = userViewModel.user,
                  networkStatus = networkStatus,
                )
              }

              else ->
                OrderScreen(
                  user = userViewModel.user,
                  networkStatus = networkStatus,
                )
            }
          }
        }

        LoginResponseType.RequestFailure -> {
          showErrorSnackbar(
            snackbarHostState = snackbarHostState,
            key = loginResponseType,
            message = stringResource(id = R.string.request_error),
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