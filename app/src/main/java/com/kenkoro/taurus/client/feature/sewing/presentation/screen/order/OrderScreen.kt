package com.kenkoro.taurus.client.feature.sewing.presentation.screen.order

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.kenkoro.taurus.client.R
import com.kenkoro.taurus.client.core.connectivity.Status
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.request.LoginRequestDto
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.response.GetUserResponseDto
import com.kenkoro.taurus.client.feature.sewing.data.util.UserProfile
import com.kenkoro.taurus.client.feature.sewing.domain.model.Order
import com.kenkoro.taurus.client.feature.sewing.domain.model.User
import com.kenkoro.taurus.client.feature.sewing.presentation.screen.order.components.OrderBottomBar
import com.kenkoro.taurus.client.feature.sewing.presentation.screen.order.components.OrderContent
import com.kenkoro.taurus.client.feature.sewing.presentation.screen.order.components.OrderTopBar
import com.kenkoro.taurus.client.feature.sewing.presentation.shared.components.ErrorSnackbar
import com.kenkoro.taurus.client.feature.sewing.presentation.shared.components.showSnackbar
import com.kenkoro.taurus.client.feature.sewing.presentation.shared.handlers.loginWithLocallyScopedCredentials
import com.kenkoro.taurus.client.feature.sewing.presentation.shared.handlers.remotelyGetUserWithLocallyScopedCredentials
import com.kenkoro.taurus.client.feature.sewing.presentation.util.LoginResponse
import com.kenkoro.taurus.client.ui.theme.AppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun OrderScreen(
  orders: LazyPagingItems<Order>,
  user: User?,
  networkStatus: Status,
  isLoginFailed: Boolean,
  loginResponse: LoginResponse,
  scope: CoroutineScope,
  onLogin: suspend (LoginRequestDto, Context, encryptSubjectAndPassword: Boolean) -> LoginResponse,
  onGetUser: suspend (String, String) -> GetUserResponseDto,
  onDeleteOrderRemotely: suspend (Int, String, String) -> Unit,
  onDeleteOrderLocally: suspend (Order) -> Unit,
  onUpsertOrderLocally: suspend (Order) -> Unit,
  onGetUserResponseChange: (GetUserResponseDto) -> Unit,
  onLoginResponseChange: (LoginResponse) -> Unit,
) {
  val context = LocalContext.current
  val snackbarHostState = remember { SnackbarHostState() }

  AppTheme {
    Scaffold(
      modifier =
        Modifier
          .statusBarsPadding()
          .navigationBarsPadding(),
      snackbarHost = {
        SnackbarHost(hostState = snackbarHostState) {
          ErrorSnackbar(
            snackbarData = it,
          )
        }
      },
      topBar = {
        OrderTopBar(networkStatus = networkStatus)
      },
      bottomBar = {
        OrderBottomBar(
          networkStatus = networkStatus,
          isLoginFailed = isLoginFailed,
        )
      },
    ) {
      Surface(
        modifier =
          Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(it),
      ) {
        if (networkStatus != Status.Available) {
          onLoginResponseChange(LoginResponse.RequestFailure)
          showSnackbar(
            snackbarHostState = snackbarHostState,
            key = networkStatus,
            message = stringResource(id = R.string.check_internet_connection),
            actionLabel = null,
          )
        } else {
          LaunchedEffect(Unit) {
            onLoginResponseChange(LoginResponse.Pending)
            withContext(Dispatchers.IO) {
              launch {
                if (loginResponse != LoginResponse.Success) {
                  loginWithLocallyScopedCredentials(
                    login = { subject, password, encryptThese ->
                      val request =
                        LoginRequestDto(
                          subject = subject,
                          password = password,
                        )
                      onLogin(request, context, encryptThese)
                    },
                    context = context,
                  ).run {
                    onLoginResponseChange(this)
                  }
                }

                remotelyGetUserWithLocallyScopedCredentials(context = context) { subject, token ->
                  try {
                    onGetUser(subject, token).run {
                      onGetUserResponseChange(this)
                    }
                  } catch (e: Exception) {
                    Log.d("kenkoro", e.message!!)
                  }
                }
              }
            }
          }
        }

        OrderContent(
          orders = orders,
          snackbarHostState = snackbarHostState,
          user = user,
          networkStatus = networkStatus,
          isLoginFailed = isLoginFailed,
          onDeleteOrderRemotely = onDeleteOrderRemotely,
          onDeleteOrderLocally = onDeleteOrderLocally,
          onUpsertOrderLocally = onUpsertOrderLocally,
          scope = scope,
        )
      }
    }
  }
}

@Preview
@Composable
private fun OrderScreenPrev() {
  val orders = flow<PagingData<Order>> { }.collectAsLazyPagingItems()
  AppTheme {
    OrderScreen(
      orders = orders,
      user = null,
      networkStatus = Status.Available,
      loginResponse = LoginResponse.Success,
      onLogin = { _, _, _ -> LoginResponse.Success },
      onGetUser = { _, _ ->
        GetUserResponseDto(
          id = -1,
          subject = "",
          password = "",
          image = "",
          firstName = "",
          lastName = "",
          email = "",
          profile = UserProfile.Admin,
          salt = "",
        )
      },
      onGetUserResponseChange = {},
      onLoginResponseChange = {},
      isLoginFailed = false,
      onDeleteOrderRemotely = { _, _, _ -> },
      onDeleteOrderLocally = { _ -> },
      onUpsertOrderLocally = { _ -> },
      scope = CoroutineScope(Dispatchers.IO),
    )
  }
}