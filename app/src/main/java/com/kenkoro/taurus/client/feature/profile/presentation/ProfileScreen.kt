package com.kenkoro.taurus.client.feature.profile.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.kenkoro.taurus.client.R
import com.kenkoro.taurus.client.feature.profile.presentation.util.ProfileScreenNavigator
import com.kenkoro.taurus.client.feature.profile.presentation.util.ProfileScreenShared
import com.kenkoro.taurus.client.feature.shared.components.BottomNavBar

@Composable
fun ProfileScreen(
  modifier: Modifier = Modifier,
  navigator: ProfileScreenNavigator,
  shared: ProfileScreenShared,
) {
  val viewModel: ProfileViewModel = hiltViewModel()

  val onClick = {
    viewModel.deleteAllUserCredentials()
    shared.resetAuthStatus()
    navigator.toAuthScreen()
    shared.restartApp()
  }

  Scaffold(
    modifier =
      modifier
        .statusBarsPadding()
        .navigationBarsPadding(),
    bottomBar = {
      if (shared.user != null) {
        BottomNavBar(
          items = shared.items,
          currentRoute = shared.currentRoute,
        )
      }
    },
  ) { innerPaddings ->
    Box(
      modifier =
        Modifier
          .fillMaxSize()
          .padding(innerPaddings),
      contentAlignment = Alignment.Center,
    ) {
      Button(
        onClick = onClick,
        colors =
          ButtonDefaults.buttonColors(
            contentColor = MaterialTheme.colorScheme.onErrorContainer,
            containerColor = MaterialTheme.colorScheme.errorContainer,
          ),
      ) {
        Text(text = stringResource(id = R.string.quit_from_account))
      }
    }
  }
}