package com.kenkoro.taurus.client.feature.e2e

import android.content.Intent
import android.os.Build
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.printToLog
import androidx.navigation.compose.rememberNavController
import com.kenkoro.taurus.client.MainActivity
import com.kenkoro.taurus.client.di.AppModule
import com.kenkoro.taurus.client.feature.shared.navigation.AppNavHost
import com.kenkoro.taurus.client.feature.shared.navigation.Screen
import com.kenkoro.taurus.client.feature.shared.navigation.util.AppNavHostUtils
import com.kenkoro.taurus.client.ui.theme.AppTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(AppModule::class)
class E2ETests {
  @get:Rule(order = 0)
  val hiltRule = HiltAndroidRule(this)

  @get:Rule(order = 1)
  val composeRule = createAndroidComposeRule<MainActivity>()

  @Before
  fun setUp() {
    hiltRule.inject()
    composeRule.activity.setContent {
      composeRule.activity.enableEdgeToEdge(
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
          val navController = rememberNavController()
          val utils =
            AppNavHostUtils(
              startDestination = { subject, password ->
                if (subject.isNotBlank() && password.isNotBlank()) {
                  Screen.OrderScreen
                } else {
                  Screen.AuthScreen
                }
              },
              exit = { composeRule.activity.finish() },
              restart = {
                val intent = Intent(composeRule.activity, MainActivity::class.java)
                composeRule.activity.startActivity(intent)
                composeRule.activity.finishAffinity()
              },
            )

          AppNavHost(navController = navController, navHostUtils = utils)
        }
      }
    }
  }

  @Test
  fun shouldLogInAsCustomer_And_SeeOrders() {
    composeRule.mainClock.advanceTimeBy(200L)
    composeRule
      .onNodeWithContentDescription("subject composable")
      .printToLog("kenkoro-ui")
  }
}