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
import com.kenkoro.taurus.client.core.CryptoManager
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.request.LoginRequest
import com.kenkoro.taurus.client.feature.sewing.presentation.login.screen.LoginViewModel
import com.kenkoro.taurus.client.feature.sewing.presentation.util.DecryptedCredentials
import com.kenkoro.taurus.client.feature.sewing.presentation.util.LocalCredentials
import com.kenkoro.taurus.client.feature.sewing.presentation.util.LoginResponseType
import com.kenkoro.taurus.client.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint
import io.ktor.http.isSuccess
import kotlinx.coroutines.runBlocking
import java.io.File
import java.io.FileInputStream
import java.nio.channels.UnresolvedAddressException

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
  private val loginViewModel: LoginViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    installSplashScreen()

    val locallyStoredCredentials =
      getDecryptedCredentials().value
        .split(" ")
        .take(LocalCredentials.CREDENTIALS_LIST_SIZE)
    val loginResponseType =
      runBlocking {
        if (locallyStoredCredentials.size greaterOrEquals 2) {
          try {
            val response =
              loginViewModel.login(
                LoginRequest(
                  subject = locallyStoredCredentials.first(),
                  password = locallyStoredCredentials.last(),
                ),
              )
            if (response.status.isSuccess()) {
              LoginResponseType.SUCCESS
            } else {
              LoginResponseType.FAILURE
            }
          } catch (_: UnresolvedAddressException) {
            LoginResponseType.NOT_INTERNET_CONNECTION
          } catch (_: Exception) {
            LoginResponseType.FAILURE
          }
        } else {
          LoginResponseType.BAD_ENCRYPTED_CREDENTIALS
        }
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
          AppNavHost(loginResponseType = loginResponseType)
        }
      }
    }
  }

  private fun getDecryptedCredentials(): DecryptedCredentials {
    val cryptoManager = CryptoManager()
    val file = File(filesDir, "${LocalCredentials.FILENAME}.txt")
    if (!file.exists()) {
      return DecryptedCredentials("")
    }
    val fis = FileInputStream(file)
    return DecryptedCredentials(cryptoManager.decrypt(fis).decodeToString())
  }

  private infix fun Int.greaterOrEquals(number: Int): Boolean = this >= number
}