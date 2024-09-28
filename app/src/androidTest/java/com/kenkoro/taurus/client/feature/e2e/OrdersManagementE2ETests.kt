package com.kenkoro.taurus.client.feature.e2e

import androidx.activity.compose.setContent
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
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
class OrdersManagementE2ETests {
  @get:Rule(order = 0)
  val hiltRule = HiltAndroidRule(this)

  @get:Rule(order = 1)
  val composeRule = createAndroidComposeRule<MainActivity>()

  @Before
  fun setUp() {
    hiltRule.inject()
    composeRule.activity.setContent {
      AppTheme {
        AppNavHost(
          navHostUtils =
            AppNavHostUtils(
              startDestination = { _, _ -> Screen.AuthScreen },
            ),
        )
      }
    }
  }

  @Test
  fun shouldLogInAsCustomer_And_SeeOrders() {
    composeRule
      .onRoot()
      .printToLog("kenkoro-ui")
  }
}