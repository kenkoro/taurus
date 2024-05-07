package com.kenkoro.taurus.client.feature.sewing.presentation.screen.order

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.kenkoro.taurus.client.R
import com.kenkoro.taurus.client.core.connectivity.NetworkStatus
import com.kenkoro.taurus.client.core.crypto.DecryptedCredentialService
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.OrderDto
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.TokenDto
import com.kenkoro.taurus.client.feature.sewing.domain.model.NewOrder
import com.kenkoro.taurus.client.feature.sewing.domain.model.Order
import com.kenkoro.taurus.client.feature.sewing.domain.model.enums.OrderStatus
import com.kenkoro.taurus.client.feature.sewing.presentation.LoginResult
import com.kenkoro.taurus.client.feature.sewing.presentation.screen.order.components.LazyOrdersContent
import com.kenkoro.taurus.client.feature.sewing.presentation.screen.order.components.OrderBottomBar
import com.kenkoro.taurus.client.feature.sewing.presentation.screen.order.components.OrderTopBar
import com.kenkoro.taurus.client.feature.sewing.presentation.shared.components.TaurusSnackbar
import com.kenkoro.taurus.client.ui.theme.AppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun OrderScreen(
  orders: LazyPagingItems<Order>,
  loginResult: LoginResult,
  onLogin: suspend () -> Result<TokenDto>,
  onLoginResult: (LoginResult) -> Unit,
  onEncryptToken: (String) -> Unit,
  onAddNewOrderLocally: suspend (NewOrder) -> Unit,
  onAddNewOrderRemotely: suspend (NewOrder) -> Result<OrderDto>,
  onDeleteOrderLocally: suspend (Order) -> Unit,
  onDeleteOrderRemotely: suspend (orderId: Int, deleterSubject: String) -> Boolean,
  decryptedCredentialService: DecryptedCredentialService,
  networkStatus: NetworkStatus,
) {
  val snackbarHostState = remember { SnackbarHostState() }
  val errorSnackbarHostState = remember { SnackbarHostState() }
  val internetSnackbarHostState = remember { SnackbarHostState() }

  val okActionLabel = stringResource(id = R.string.ok)
  val requestErrorMessage = stringResource(id = R.string.request_error)
  val internetConnectionErrorMessage = stringResource(id = R.string.check_internet_connection)

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
      modifier = Modifier
        .statusBarsPadding()
        .navigationBarsPadding(),
      topBar = {
        OrderTopBar(
          networkStatus = networkStatus,
          modifier = Modifier.background(Color.Red),
        )
      },
      bottomBar = {
        OrderBottomBar(
          networkStatus = networkStatus,
          modifier = Modifier.background(Color.Yellow)
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
            LaunchedEffect(networkStatus) {
              launch {
                internetSnackbarHostState.showSnackbar(
                  message = internetConnectionErrorMessage,
                  duration = SnackbarDuration.Indefinite,
                )
              }
            }
          }

          if (loginResult == LoginResult.NotLoggedYet) {
            LaunchedEffect(Unit) {
              withContext(Dispatchers.IO) {
                val result = onLogin()
                result.onSuccess { dto ->
                  onLoginResult(LoginResult.Success)
                  onEncryptToken(dto.token)
                }

                result.onFailure {
                  errorSnackbarHostState.showSnackbar(
                    message = requestErrorMessage,
                    actionLabel = okActionLabel,
                  )
                }
              }
            }
          }

          LazyOrdersContent(
            orders = orders,
            onAddNewOrderLocally = onAddNewOrderLocally,
            onDeleteOrderLocally = onDeleteOrderLocally,
            onDeleteOrderRemotely = onDeleteOrderRemotely,
            snackbarHostState = snackbarHostState,
            errorSnackbarHostState = errorSnackbarHostState,
            decryptedCredentialService = decryptedCredentialService,
            networkStatus = networkStatus,
            modifier = Modifier.background(Color.Green),
          )
        }
      },
    )
  }
}

@Preview
@Composable
private fun OrderScreenPrev() {
  val context = LocalContext.current
  val decryptedCredentialService = DecryptedCredentialService(context)

  val order =
    Order(
      recordId = 0,
      orderId = 0,
      customer = "Customer",
      date = 0L,
      title = "Title",
      model = "Model",
      size = "Size",
      color = "Color",
      category = "Category",
      quantity = 0,
      status = OrderStatus.Idle,
      creatorId = 0,
    )
  val orders = flow {
    emit(
      PagingData.from(
        listOf(
          order,
          order,
          order,
        )
      )
    )
  }.collectAsLazyPagingItems()
  val orderDto = OrderDto(order)

  AppTheme {
    OrderScreen(
      orders = orders,
      loginResult = LoginResult.Success,
      onLogin = { Result.success(TokenDto("")) },
      onLoginResult = {},
      onEncryptToken = {},
      onAddNewOrderLocally = { _ -> },
      onAddNewOrderRemotely = { _ -> Result.success(orderDto) },
      onDeleteOrderLocally = { _ -> },
      onDeleteOrderRemotely = { _, _ -> false },
      decryptedCredentialService = decryptedCredentialService,
      networkStatus = NetworkStatus.Available,
    )
  }
}