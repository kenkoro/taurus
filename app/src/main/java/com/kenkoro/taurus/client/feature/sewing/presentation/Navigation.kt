package com.kenkoro.taurus.client.feature.sewing.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.response.GetUserResponseDto
import com.kenkoro.taurus.client.feature.sewing.presentation.screen.dashboard.DashboardScreen
import com.kenkoro.taurus.client.feature.sewing.presentation.screen.login.LoginFieldsViewModel
import com.kenkoro.taurus.client.feature.sewing.presentation.screen.login.LoginScreen
import com.kenkoro.taurus.client.feature.sewing.presentation.screen.order.OrderViewModel
import com.kenkoro.taurus.client.feature.sewing.presentation.screen.user.UserViewModel
import com.kenkoro.taurus.client.feature.sewing.presentation.util.DecryptedCredentials
import com.kenkoro.taurus.client.feature.sewing.presentation.util.LocalCredentials
import com.kenkoro.taurus.client.feature.sewing.presentation.util.Screen
import io.ktor.client.call.body

@Composable
fun AppNavHost(
  navController: NavHostController = rememberNavController(),
  startDestination: (String, String) -> Screen,
) {
  val context = LocalContext.current
  val locallyStoredSubject =
    DecryptedCredentials.getDecryptedCredential(
      filename = LocalCredentials.SUBJECT_FILENAME,
      context = context,
    ).value
  val locallyStoredPassword =
    DecryptedCredentials.getDecryptedCredential(
      filename = LocalCredentials.PASSWORD_FILENAME,
      context = context,
    ).value

  val loginFieldsViewModel: LoginFieldsViewModel = hiltViewModel()
  val userViewModel: UserViewModel = hiltViewModel()
  val orderViewModel: OrderViewModel = hiltViewModel()
  NavHost(
    navController = navController,
    startDestination = startDestination(locallyStoredSubject, locallyStoredPassword).route,
  ) {
    composable(route = Screen.LoginScreen.route) {
      LoginScreen(
        onLoginNavigate = {
          navController.navigate(Screen.DashboardScreen.route)
        },
        subject = loginFieldsViewModel.subject,
        onSubjectChange = loginFieldsViewModel::subject,
        password = loginFieldsViewModel.password,
        onPasswordChange = loginFieldsViewModel::password,
        onLoginAndEncryptCredentials = { loginRequestDto, context, encryptSubjectAndPassword ->
          loginFieldsViewModel.loginAndEncryptCredentials(
            loginRequestDto,
            context,
            encryptSubjectAndPassword,
          )
        },
      )
    }
    composable(route = Screen.DashboardScreen.route) {
      DashboardScreen(
        onDashboardNavigate = {
          navController.navigate(Screen.LoginScreen.route)
        },
        onLoginAndEncryptCredentials = { loginRequestDto, context, encryptSubjectAndPassword ->
          loginFieldsViewModel.loginAndEncryptCredentials(
            loginRequestDto,
            context,
            encryptSubjectAndPassword,
          )
        },
        onGetUser = { subject, token ->
          userViewModel
            .getUser(subject, token)
            .body<GetUserResponseDto>()
        },
        onGetUserResponseChange = userViewModel::onGetUserResponseDto,
        user = userViewModel.user,
        orders = orderViewModel.orderPagingFlow.collectAsLazyPagingItems(),
      )
    }
  }
}