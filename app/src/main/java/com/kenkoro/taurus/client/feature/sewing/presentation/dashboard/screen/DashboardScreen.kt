package com.kenkoro.taurus.client.feature.sewing.presentation.dashboard.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kenkoro.taurus.client.R
import com.kenkoro.taurus.client.feature.sewing.presentation.MainViewModel
import com.kenkoro.taurus.client.feature.sewing.presentation.dashboard.screen.components.BottomBarHost
import com.kenkoro.taurus.client.feature.sewing.presentation.dashboard.screen.components.ErrorSnackbar
import com.kenkoro.taurus.client.feature.sewing.presentation.order.screen.OrderScreen
import com.kenkoro.taurus.client.feature.sewing.presentation.user.screen.UserScreen
import com.kenkoro.taurus.client.feature.sewing.presentation.util.LoginResponseType
import com.kenkoro.taurus.client.ui.theme.AppTheme

object BottomBarHostIndices {
  const val ORDER_SCREEN = 1
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DashboardScreen(
  onLoginNavigate: () -> Unit = {},
  onNavigateUp: () -> Unit = {},
  mainViewModel: MainViewModel = hiltViewModel(),
) {
  val snackbarHostState = remember { SnackbarHostState() }

  AppTheme {
    Scaffold(
      snackbarHost = {
        SnackbarHost(hostState = snackbarHostState) {
          Snackbar(
            modifier = Modifier.padding(bottom = 20.dp),
            snackbarData = it,
            shape = RoundedCornerShape(30.dp),
            containerColor = MaterialTheme.colorScheme.error,
            contentColor = MaterialTheme.colorScheme.onError,
            dismissActionContentColor = MaterialTheme.colorScheme.onError,
            actionColor = MaterialTheme.colorScheme.onError,
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
        when (mainViewModel.loginResponseType.value) {
          LoginResponseType.SUCCESS -> {
            BottomBarHost { index ->
              when (index) {
                BottomBarHostIndices.ORDER_SCREEN -> OrderScreen()
                else -> UserScreen()
              }
            }
          }

          LoginResponseType.NOT_INTERNET_CONNECTION -> {
            val message = stringResource(id = R.string.check_internet_connection)
            ErrorSnackbar(
              snackbarHostState = snackbarHostState,
              message = message,
              onDismissed = onNavigateUp,
              onActionPerformed = {},
            )
          }

          LoginResponseType.REQUEST_FAILURE -> {
            val message = stringResource(id = R.string.request_error)
            ErrorSnackbar(
              snackbarHostState = snackbarHostState,
              message = message,
              onDismissed = onNavigateUp,
              onActionPerformed = {},
            )
          }

          LoginResponseType.API_NOT_FOUND -> {
            val message = stringResource(id = R.string.login_not_found)
            ErrorSnackbar(
              snackbarHostState = snackbarHostState,
              message = message,
              onDismissed = onNavigateUp,
              onActionPerformed = {},
            )
          }

          LoginResponseType.FAILURE -> {
            onLoginNavigate()
          }

          LoginResponseType.BAD_DECRYPTED_CREDENTIALS -> {
            onLoginNavigate()
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