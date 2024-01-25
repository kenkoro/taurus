package com.kenkoro.taurus.mobile_client.feature_login.presentation.login_screen

import android.view.HapticFeedbackConstants
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kenkoro.taurus.mobile_client.feature_login.presentation.login_screen.components.LoginField
import com.kenkoro.taurus.mobile_client.feature_login.presentation.login_screen.components.PasswordField
import com.kenkoro.taurus.mobile_client.ui.theme.TaurusTheme
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
  navController: NavController,
  viewModel: LoginViewModel = hiltViewModel(),
  modifier: Modifier = Modifier
) {
  val username = viewModel.username
  val password = viewModel.password
  val snackbarHostState = remember { SnackbarHostState() }
  val scope = rememberCoroutineScope()
  val localView = LocalView.current

  TaurusTheme {
    Scaffold(
      snackbarHost = {
        SnackbarHost(hostState = snackbarHostState)
      },
      modifier = modifier
    ) {
      Surface(
        modifier = Modifier
          .padding(it)
          .fillMaxSize()
      ) {
        Column(
          modifier = Modifier
            .fillMaxSize()
            .padding(25.dp),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
        ) {
          LoginField(userName = username, modifier = Modifier.fillMaxWidth())
          Spacer(modifier = Modifier.height(16.dp))
          PasswordField(password = password, modifier = Modifier.fillMaxWidth())
          Spacer(modifier = Modifier.height(16.dp))
          Button(
            onClick = {
              scope.launch {
                localView.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
                val response = viewModel.onEvent(UserEvent.AUTH)

                var message = ""
                message += if (response.isOk()) {
                  "Works!"
                } else {
                  "Failed!"
                }

                snackbarHostState.showSnackbar(
                  message = message,
                  withDismissAction = true,
                  duration = SnackbarDuration.Short
                )
              }
            },
          ) {
            Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = null)
          }
        }
      }
    }
  }
}