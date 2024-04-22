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
import androidx.compose.material3.SnackbarDuration
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
import com.kenkoro.taurus.client.core.connectivity.NetworkStatus
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.request.LoginRequestDto
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.request.OrderRequestDto
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.response.GetUserResponseDto
import com.kenkoro.taurus.client.feature.sewing.data.util.UserProfile
import com.kenkoro.taurus.client.feature.sewing.domain.model.Order
import com.kenkoro.taurus.client.feature.sewing.domain.model.User
import com.kenkoro.taurus.client.feature.sewing.presentation.screen.order.components.OrderBottomBar
import com.kenkoro.taurus.client.feature.sewing.presentation.screen.order.components.OrderContent
import com.kenkoro.taurus.client.feature.sewing.presentation.screen.order.components.OrderTopBar
import com.kenkoro.taurus.client.feature.sewing.presentation.shared.components.TaurusSnackbar
import com.kenkoro.taurus.client.feature.sewing.presentation.util.DecryptedCredentialService
import com.kenkoro.taurus.client.feature.sewing.presentation.util.LoginResponse
import com.kenkoro.taurus.client.ui.theme.AppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun OrderScreen(
  orders: LazyPagingItems<Order>,
  user: User?,
  loginFailed: Boolean,
  loginResponse: LoginResponse,
  networkStatus: NetworkStatus,
  onLogin: suspend (LoginRequestDto, Context, encryptSubjectAndPassword: Boolean) -> LoginResponse,
  onGetUser: suspend (String, String) -> GetUserResponseDto,
  onDeleteOrderRemotely: suspend (Int, String, String) -> Unit,
  onDeleteOrderLocally: suspend (Order) -> Unit,
  onUpsertOrderLocally: suspend (Order) -> Unit,
  onUpsertOrderRemotely: suspend (OrderRequestDto, String) -> Unit,
  onGetUserResponseChange: (GetUserResponseDto) -> Unit,
  onLoginResponseChange: (LoginResponse) -> Unit,
) {
  val context = LocalContext.current
  val snackbarHostState = remember { SnackbarHostState() }
  val errorSnackbarHostState = remember { SnackbarHostState() }
  val internetSnackbarHostState = remember { SnackbarHostState() }

  val internetConnectionErrorMessage = stringResource(id = R.string.check_internet_connection)
  val credentialService = DecryptedCredentialService(context)

  AppTheme {
    Scaffold(
      snackbarHost = {
        TaurusSnackbar(
          snackbarHostState = snackbarHostState,
          onDismiss = { snackbarHostState.currentSnackbarData?.dismiss() },
        )

        TaurusSnackbar(
          snackbarHostState = errorSnackbarHostState,
          onDismiss = { errorSnackbarHostState.currentSnackbarData?.dismiss() },
          containerColor = MaterialTheme.colorScheme.errorContainer,
          contentColor = MaterialTheme.colorScheme.onErrorContainer,
        )

        TaurusSnackbar(
          snackbarHostState = internetSnackbarHostState,
          onDismiss = { internetSnackbarHostState.currentSnackbarData?.dismiss() },
          containerColor = MaterialTheme.colorScheme.errorContainer,
          contentColor = MaterialTheme.colorScheme.onErrorContainer,
          centeredContent = true,
        )
      },
      modifier =
        Modifier
          .statusBarsPadding()
          .navigationBarsPadding(),
      topBar = {
        OrderTopBar(networkStatus = networkStatus)
      },
      bottomBar = {
        OrderBottomBar(
          networkStatus = networkStatus,
          isLoginFailed = loginFailed,
          onUpsertOrderLocally = onUpsertOrderLocally,
          onUpsertOrderRemotely = onUpsertOrderRemotely,
        )
      },
      content = {
        Surface(
          modifier =
            Modifier
              .fillMaxSize()
              .background(MaterialTheme.colorScheme.background)
              .padding(it),
        ) {
          if (networkStatus != NetworkStatus.Available) {
            onLoginResponseChange(LoginResponse.RequestFailure)
            LaunchedEffect(networkStatus) {
              launch {
                internetSnackbarHostState.showSnackbar(
                  message = internetConnectionErrorMessage,
                  duration = SnackbarDuration.Indefinite,
                )
              }
            }
          } else {
            LaunchedEffect(Unit) {
              withContext(Dispatchers.IO) {
                launch {
                  if (loginResponse != LoginResponse.Success) {
                    val loginDto =
                      LoginRequestDto(
                        subject = credentialService.storedSubject(),
                        password = credentialService.storedPassword(),
                      )
                    onLogin(
                      loginDto,
                      context,
                      false,
                    ).run {
                      onLoginResponseChange(this)
                    }

                    try {
                      onGetUser(
                        credentialService.storedSubject(),
                        credentialService.storedToken(),
                      ).run {
                        onGetUserResponseChange(this)
                      }
                    } catch (e: Exception) {
                      Log.d("kenkoro", e.message!!)
                    }
                  }
                }
              }
            }

            OrderContent(
              orders = orders,
              user = user,
              loginFailed = loginFailed,
              onDeleteOrderRemotely = onDeleteOrderRemotely,
              onDeleteOrderLocally = onDeleteOrderLocally,
              onUpsertOrderLocally = onUpsertOrderLocally,
              networkStatus = networkStatus,
              errorSnackbarHostState = errorSnackbarHostState,
              snackbarHostState = snackbarHostState,
            )
          }
        }
      },
    )
  }
}

@Preview
@Composable
private fun OrderScreenPrev() {
  val orders = flow<PagingData<Order>> {}.collectAsLazyPagingItems()
  AppTheme {
    OrderScreen(
      orders = orders,
      user = null,
      networkStatus = NetworkStatus.Available,
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
      loginFailed = false,
      onDeleteOrderRemotely = { _, _, _ -> },
      onDeleteOrderLocally = { _ -> },
      onUpsertOrderLocally = { _ -> },
      onUpsertOrderRemotely = { _, _ -> },
    )
  }
}