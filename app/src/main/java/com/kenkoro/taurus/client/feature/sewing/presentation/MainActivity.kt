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
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.request.LoginRequest
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.response.AuthResponse
import com.kenkoro.taurus.client.feature.sewing.presentation.login.screen.LoginViewModel
import com.kenkoro.taurus.client.feature.sewing.presentation.util.DecryptedCredentials
import com.kenkoro.taurus.client.feature.sewing.presentation.util.EncryptedCredentials
import com.kenkoro.taurus.client.feature.sewing.presentation.util.LocalCredentials
import com.kenkoro.taurus.client.feature.sewing.presentation.util.LoginResponseType
import com.kenkoro.taurus.client.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint
import io.ktor.client.call.body
import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess
import kotlinx.coroutines.runBlocking
import java.nio.channels.UnresolvedAddressException

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
    val loginResponseType =
      runBlocking {
        if (locallyStoredSubject.isNotBlank() && locallyStoredPassword.isNotBlank()) {
          try {
            val response =
              loginViewModel.login(
                LoginRequest(
                  subject = locallyStoredSubject,
                  password = locallyStoredPassword,
                ),
              )

            val status = response.status
            if (status.isSuccess()) {
              EncryptedCredentials.encryptCredential(
                credential =
                  try {
                    response.body<AuthResponse>().token
                  } catch (_: Exception) {
                    ""
                  },
                filename = LocalCredentials.TOKEN_FILENAME,
                context = applicationContext,
              )
              LoginResponseType.SUCCESS
            } else {
              if (status == HttpStatusCode.NotFound) {
                LoginResponseType.API_NOT_FOUND
              } else {
                LoginResponseType.FAILURE
              }
            }
          } catch (_: UnresolvedAddressException) {
            LoginResponseType.NOT_INTERNET_CONNECTION
          } catch (_: Exception) {
            LoginResponseType.REQUEST_FAILURE
          }
        } else {
          LoginResponseType.BAD_DECRYPTED_CREDENTIALS
        }
      }

    mainViewModel.saveLoginResponseType(loginResponseType)

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