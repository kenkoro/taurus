package com.kenkoro.taurus.client.feature.login.presentation.login.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.kenkoro.taurus.client.feature.login.presentation.login.screen.components.LoginBlock
import com.kenkoro.taurus.client.ui.theme.TaurusTheme

@Composable
fun LoginScreen(
  navController: NavController,
  viewModel: LoginViewModel = hiltViewModel(),
  @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
  val snackbarHostState = remember { SnackbarHostState() }

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
        LoginBlock(
          navController = navController,
          snackbarHostState = snackbarHostState
        )
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
fun LoginBlockPreview() {
  val navController = rememberNavController()
  val snackbarHostState = remember { SnackbarHostState() }

  LoginBlock(navController = navController, snackbarHostState = snackbarHostState)
}