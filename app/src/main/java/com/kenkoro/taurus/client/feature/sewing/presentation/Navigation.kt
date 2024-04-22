package com.kenkoro.taurus.client.feature.sewing.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.kenkoro.taurus.client.core.connectivity.ConnectivityObserver
import com.kenkoro.taurus.client.core.connectivity.NetworkConnectivityObserver
import com.kenkoro.taurus.client.core.connectivity.NetworkStatus
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.response.GetUserResponseDto
import com.kenkoro.taurus.client.feature.sewing.presentation.screen.login.LoginScreen
import com.kenkoro.taurus.client.feature.sewing.presentation.screen.login.UserViewModel
import com.kenkoro.taurus.client.feature.sewing.presentation.screen.order.OrderScreen
import com.kenkoro.taurus.client.feature.sewing.presentation.screen.order.OrderViewModel
import com.kenkoro.taurus.client.feature.sewing.presentation.util.DecryptedCredential
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
    DecryptedCredential.decrypt(
      filename = LocalCredentials.SUBJECT_FILENAME,
      context = context,
    ).value
  val locallyStoredPassword =
    DecryptedCredential.decrypt(
      filename = LocalCredentials.PASSWORD_FILENAME,
      context = context,
    ).value

  val networkConnectivityObserver: ConnectivityObserver = NetworkConnectivityObserver(context)
  val networkStatus by networkConnectivityObserver
    .observer()
    .collectAsState(initial = NetworkStatus.Unavailable)

  val userViewModel: UserViewModel = hiltViewModel()
  val orderViewModel: OrderViewModel = hiltViewModel()
  NavHost(
    navController = navController,
    startDestination = startDestination(locallyStoredSubject, locallyStoredPassword).route,
  ) {
    composable(route = Screen.LoginScreen.route) {
      LoginScreen(
        networkStatus = networkStatus,
        onLoginNavigate = {
          navController.navigate(Screen.OrderScreen.route)
        },
        subject = userViewModel.subject,
        onSubjectChange = userViewModel::subject,
        password = userViewModel.password,
        onPasswordChange = userViewModel::password,
        onLogin = { loginRequestDto, context, encryptSubjectAndPassword ->
          userViewModel.login(
            loginRequestDto,
            context,
            encryptSubjectAndPassword,
          )
        },
        onLoginResponseChange = userViewModel::loginResponse,
      )
    }
    composable(route = Screen.OrderScreen.route) {
      OrderScreen(
        networkStatus = networkStatus,
        orders = orderViewModel.orderPagingFlow.collectAsLazyPagingItems(),
        user = userViewModel.user,
        loginResponse = userViewModel.loginResponse,
        isLoginFailed = userViewModel.isLoginFailed,
        onLogin = { loginRequestDto, context, encryptSubjectAndPassword ->
          userViewModel.login(
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
        onLoginResponseChange = userViewModel::loginResponse,
        onDeleteOrderRemotely = { orderId, token, deleterSubject ->
          orderViewModel.deleteOrderRemotely(orderId, token, deleterSubject)
        },
        onDeleteOrderLocally = orderViewModel::deleteOrderLocally,
        onUpsertOrderLocally = orderViewModel::upsertOrderLocally,
        onUpsertOrderRemotely = { request, token ->
          orderViewModel.upsertOrderRemotely(request, token)
        },
      )
    }
  }
}