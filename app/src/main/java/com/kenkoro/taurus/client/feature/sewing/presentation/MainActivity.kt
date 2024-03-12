package com.kenkoro.taurus.client.feature.sewing.presentation

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.kenkoro.taurus.client.feature.sewing.presentation.login.screen.LoginViewModel
import com.kenkoro.taurus.client.feature.sewing.presentation.shared.handlers.LoginResponseHandler
import com.kenkoro.taurus.client.feature.sewing.presentation.shared.handlers.ResponseHandler
import com.kenkoro.taurus.client.feature.sewing.presentation.util.DecryptedCredentials
import com.kenkoro.taurus.client.feature.sewing.presentation.util.LocalCredentials
import com.kenkoro.taurus.client.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
  private val loginViewModel: LoginViewModel by viewModels()
  private val mainViewModel: MainViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    installSplashScreen()

    val locallyStoredSubject =
      DecryptedCredentials.getDecryptedCredential(
        filename = LocalCredentials.SUBJECT_FILENAME,
        context = applicationContext,
      ).value
    val locallyStoredPassword =
      DecryptedCredentials.getDecryptedCredential(
        filename = LocalCredentials.PASSWORD_FILENAME,
        context = applicationContext,
      ).value
    lifecycleScope.launch {
      val handler: ResponseHandler = LoginResponseHandler()
      mainViewModel.loginResponseType(
        loginResponseType =
          handler.handle(
            subject = locallyStoredSubject,
            password = locallyStoredPassword,
            context = applicationContext,
            loginViewModel,
          ),
      )
    }

    setContent {
      enableEdgeToEdge(
        statusBarStyle =
          SystemBarStyle.auto(
            lightScrim = Color.Transparent.toArgb(),
            darkScrim = Color.Transparent.toArgb(),
            detectDarkMode = { resources ->
              return@auto if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                resources.configuration.isNightModeActive
              } else {
                false
              }
            },
          ),
      )

      AppTheme {
        Surface(
          modifier = Modifier.fillMaxSize(),
          color = MaterialTheme.colorScheme.background,
        ) {
          AppNavHost(mainViewModel = mainViewModel)
        }
      }
    }
  }
}