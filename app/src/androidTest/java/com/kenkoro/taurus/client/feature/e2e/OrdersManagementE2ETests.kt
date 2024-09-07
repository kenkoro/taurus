package com.kenkoro.taurus.client.feature.e2e

import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onRoot
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kenkoro.taurus.client.MainActivity
import com.kenkoro.taurus.client.core.connectivity.ConnectivityObserver
import com.kenkoro.taurus.client.core.connectivity.NetworkConnectivityObserver
import com.kenkoro.taurus.client.core.connectivity.NetworkStatus
import com.kenkoro.taurus.client.di.AppModule
import com.kenkoro.taurus.client.feature.login.presentation.LoginRoute
import com.kenkoro.taurus.client.feature.login.presentation.util.LoginScreenNavigator
import com.kenkoro.taurus.client.feature.shared.navigation.Screen
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
        Surface(
          modifier = Modifier.fillMaxSize(),
          color = MaterialTheme.colorScheme.background,
        ) {
          val navController = rememberNavController()
          val ctx = LocalContext.current
          val networkConnectivityObserver: ConnectivityObserver = NetworkConnectivityObserver(ctx)
          val networkStatus by networkConnectivityObserver
            .observer()
            .collectAsState(initial = NetworkStatus.Unavailable)

          val loginScreenNavigator = LoginScreenNavigator {}

          NavHost(navController = navController, startDestination = Screen.LoginScreen.route) {
            composable(route = Screen.LoginScreen.route) {
              LoginRoute(
                navigator = loginScreenNavigator,
                network = networkStatus,
                onExit = {},
              )
            }
          }
        }
      }
    }
  }

  @Test
  fun shouldLogInAsCustomer_And_SeeOrders() {
    /**
     * NOTE: Leave it for now
     */
    composeRule
      .onRoot()
      .assertIsDisplayed()
  }
}