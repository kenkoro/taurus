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
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.kenkoro.taurus.client.MainActivity
import com.kenkoro.taurus.client.core.androidTest.E2ETestTags
import com.kenkoro.taurus.client.core.connectivity.ConnectivityObserver
import com.kenkoro.taurus.client.core.connectivity.NetworkConnectivityObserver
import com.kenkoro.taurus.client.core.connectivity.NetworkStatus
import com.kenkoro.taurus.client.di.AppModule
import com.kenkoro.taurus.client.feature.login.presentation.LoginScreen
import com.kenkoro.taurus.client.feature.login.presentation.LoginViewModel
import com.kenkoro.taurus.client.feature.login.presentation.util.LoginScreenNavigator
import com.kenkoro.taurus.client.feature.login.presentation.util.LoginScreenRemoteHandler
import com.kenkoro.taurus.client.feature.login.presentation.util.LoginScreenUtils
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

          /**
           * TODO: Try to use it in the ScreenRoute composable next time
           */
          val loginViewModel: LoginViewModel = hiltViewModel()

          val ctx = LocalContext.current
          val networkConnectivityObserver: ConnectivityObserver = NetworkConnectivityObserver(ctx)
          val networkStatus by networkConnectivityObserver
            .observer()
            .collectAsState(initial = NetworkStatus.Unavailable)

          val loginScreenRemoteHandler = LoginScreenRemoteHandler(loginViewModel::login)
          val loginScreenNavigator =
            LoginScreenNavigator {
              navController.navigate(Screen.LoginScreen.route)
            }
          val loginScreenUtils =
            LoginScreenUtils(
              subject = loginViewModel.subject,
              password = loginViewModel.password,
              network = networkStatus,
              encryptAllUserCredentials = loginViewModel::encryptAll,
              exit = {},
              showErrorTitle = loginViewModel::showErrorTitle,
            )

          LoginScreen(
            remoteHandler = loginScreenRemoteHandler,
            navigator = loginScreenNavigator,
            utils = loginScreenUtils,
          )
        }
      }
    }
  }

  @Test
  fun shouldLogInAsCustomer_And_SeeOrders() {
    assertThatLoginScreenComponentsAreDisplayed()
  }

  private fun performTextInputOn(
    testTag: String,
    text: String = "",
  ) {
    composeRule
      .onNodeWithTag(testTag)
      .performTextInput(text)
  }

  private fun logInAsCustomer() {
    performTextInputOn(E2ETestTags.SUBJECT_TEXT_FIELD, "customer")
    performTextInputOn(E2ETestTags.PASSWORD_TEXT_FIELD, "1234")

    composeRule
      .onNodeWithContentDescription("IconForLoginButton")
      .performClick()
  }

  private fun assertThatLoginScreenComponentsAreDisplayed() {
    composeRule
      .onNodeWithTag(E2ETestTags.SUBJECT_TEXT_FIELD)
      .assertIsDisplayed()
    composeRule
      .onNodeWithTag(E2ETestTags.PASSWORD_TEXT_FIELD)
      .assertIsDisplayed()
  }
}