package com.kenkoro.taurus.client.feature.sewing.presentation.login.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kenkoro.taurus.client.feature.sewing.presentation.login.screen.components.HelpBlock
import com.kenkoro.taurus.client.feature.sewing.presentation.login.screen.components.LoginBlock
import com.kenkoro.taurus.client.feature.sewing.presentation.shared.components.ErrorSnackbar
import com.kenkoro.taurus.client.ui.theme.AppTheme

@Composable
fun LoginScreen(
  onLoginNavigate: () -> Unit,
  loginViewModel: LoginViewModel = hiltViewModel(),
) {
  val snackbarHostState = remember { SnackbarHostState() }

  AppTheme {
    Scaffold(
      snackbarHost = {
        SnackbarHost(hostState = snackbarHostState) {
          ErrorSnackbar(
            modifier = Modifier.padding(bottom = 80.dp),
            snackbarData = it,
          )
        }
      },
    ) {
      Surface(
        modifier =
          Modifier
            .fillMaxSize()
            .padding(it),
      ) {
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
        ) {
          LoginBlock(
            loginViewModel = loginViewModel,
            modifier =
              Modifier
                .width(320.dp)
                .weight(9F),
            onLoginNavigate = onLoginNavigate,
            snackbarHostState = snackbarHostState,
          )
          HelpBlock(
            modifier = Modifier.weight(1F),
            snackbarHostState = snackbarHostState,
          )
          Spacer(modifier = Modifier.height(10.dp))
        }
      }
    }
  }
}