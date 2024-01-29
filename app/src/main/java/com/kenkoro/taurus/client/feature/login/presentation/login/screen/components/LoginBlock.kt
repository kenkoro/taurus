package com.kenkoro.taurus.client.feature.login.presentation.login.screen.components

import android.view.HapticFeedbackConstants
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kenkoro.taurus.client.feature.login.presentation.login.screen.LoginViewModel
import com.kenkoro.taurus.client.feature.login.presentation.login.screen.UserEvent
import com.kenkoro.taurus.client.feature.login.presentation.util.Screen
import kotlinx.coroutines.launch

@Composable
fun LoginBlock(
  navController: NavController,
  snackbarHostState: SnackbarHostState,
  viewModel: LoginViewModel = hiltViewModel()
) {
  val username = viewModel.username
  val password = viewModel.password
  val scope = rememberCoroutineScope()
  val localView = LocalView.current

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
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Button(onClick = { viewModel.resetUserCredentials() }) {
        Icon(imageVector = Icons.Default.Refresh, contentDescription = null)
      }
      Button(
        onClick = {
          scope.launch {
            localView.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
            val response = viewModel.onApiEvent(UserEvent.AUTH)

            var message = ""
            if (response.isOk()) {
              message += "Works!"
              navController.navigate(Screen.CustomerScreen.route)
            } else {
              message += "Failed!"
            }

            snackbarHostState.showSnackbar(
              message = message,
              withDismissAction = true,
              duration = SnackbarDuration.Short
            )
          }
        }
      ) {
        Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = null)
      }
    }
  }
}