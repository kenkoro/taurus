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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.kenkoro.taurus.client.R
import com.kenkoro.taurus.client.core.connectivity.NetworkStatus
import com.kenkoro.taurus.client.feature.sewing.data.source.local.UserEntity
import com.kenkoro.taurus.client.feature.sewing.data.source.mappers.toOrderDto
import com.kenkoro.taurus.client.feature.sewing.data.source.mappers.toUserDto
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.NewOrderDto
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.OrderDto
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.TokenDto
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.UserDto
import com.kenkoro.taurus.client.feature.sewing.domain.model.NewOrder
import com.kenkoro.taurus.client.feature.sewing.domain.model.Order
import com.kenkoro.taurus.client.feature.sewing.domain.model.User
import com.kenkoro.taurus.client.feature.sewing.domain.model.enums.OrderStatus
import com.kenkoro.taurus.client.feature.sewing.domain.model.enums.UserProfile
import com.kenkoro.taurus.client.feature.sewing.presentation.screen.order.components.OrderBottomBar
import com.kenkoro.taurus.client.feature.sewing.presentation.screen.order.components.OrderContent
import com.kenkoro.taurus.client.feature.sewing.presentation.screen.order.components.OrderTopBar
import com.kenkoro.taurus.client.feature.sewing.presentation.screen.util.LoginResult
import com.kenkoro.taurus.client.feature.sewing.presentation.shared.components.TaurusSnackbar
import com.kenkoro.taurus.client.ui.theme.AppTheme
import kotlinx.coroutines.flow.flow

@Composable
fun OrderScreen(
  user: User?,
  onUser: (User) -> Unit,
  orders: LazyPagingItems<Order>,
  loginResult: LoginResult,
  onLoginResult: (LoginResult) -> Unit,
  onAddNewUserLocally: suspend (UserEntity) -> Unit,
  onAddNewOrderLocally: suspend (NewOrder) -> Unit,
  onDeleteOrderLocally: suspend (Order) -> Unit,
  onEditOrderLocally: suspend (NewOrder) -> Unit,
  onLogin: suspend (storedSubject: String, storedPassword: String) -> Result<TokenDto>,
  onGetUserRemotely: suspend (subject: String, token: String) -> Result<UserDto>,
  onAddNewOrderRemotely: suspend (NewOrder) -> Result<OrderDto>,
  onDeleteOrderRemotely: suspend (orderId: Int, deleterSubject: String) -> Boolean,
  onEditOrderRemotely: suspend (NewOrderDto, Int, String, String) -> Boolean,
  onEncryptToken: (String) -> Unit,
  onDecryptSubjectAndPassword: () -> Pair<String, String>,
  onDecryptToken: () -> String,
  networkStatus: NetworkStatus,
) {
  val snackbarHostState = remember { SnackbarHostState() }
  val errorSnackbarHostState = remember { SnackbarHostState() }
  val internetSnackbarHostState = remember { SnackbarHostState() }

  val internetConnectionErrorMessage = stringResource(id = R.string.check_internet_connection)
  val orderWasDeletedMessage = stringResource(id = R.string.order_was_deleted)
  val paginatedOrdersErrorMessage = stringResource(id = R.string.paginated_orders_error)
  val loginErrorMessage = stringResource(id = R.string.login_fail)

  val cancelOrderDeletionLabel = stringResource(id = R.string.cancel)
  val okActionLabel = stringResource(id = R.string.ok)

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
        OrderTopBar(
          networkStatus = networkStatus,
        )
      },
      bottomBar = {
        // TODO: onAddNewOrderRemotely callback
        OrderBottomBar(
          networkStatus = networkStatus,
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
          OrderContent(
            user = user,
            onUser = onUser,
            orders = orders,
            loginResult = loginResult,
            onLoginResult = onLoginResult,
            onAddNewUserLocally = onAddNewUserLocally,
            onAddNewOrderLocally = onAddNewOrderLocally,
            onDeleteOrderLocally = onDeleteOrderLocally,
            onEditOrderLocally = onEditOrderLocally,
            onLogin = onLogin,
            onGetUserRemotely = onGetUserRemotely,
            onDeleteOrderRemotely = onDeleteOrderRemotely,
            onEditOrderRemotely = onEditOrderRemotely,
            onInternetConnectionErrorShowSnackbar = {
              internetSnackbarHostState.showSnackbar(
                message = internetConnectionErrorMessage,
                duration = SnackbarDuration.Indefinite,
              )
            },
            onLoginErrorShowSnackbar = {
              errorSnackbarHostState.showSnackbar(
                message = loginErrorMessage,
                actionLabel = okActionLabel,
              )
            },
            onDeleteOrderShowSnackbar = {
              snackbarHostState.showSnackbar(
                message = orderWasDeletedMessage,
                actionLabel = cancelOrderDeletionLabel,
                duration = SnackbarDuration.Short,
              )
            },
            onAppendNewOrdersErrorShowSnackbar = {
              errorSnackbarHostState.showSnackbar(
                message = paginatedOrdersErrorMessage,
                actionLabel = okActionLabel,
              )
            },
            onEncryptToken = onEncryptToken,
            onDecryptSubjectAndPassword = onDecryptSubjectAndPassword,
            onDecryptToken = onDecryptToken,
            networkStatus = networkStatus,
          )
        }
      },
    )
  }
}

@Preview
@Composable
private fun OrderScreenPrev() {
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
  val orders =
    flow {
      emit(
        PagingData.from(
          listOf(
            order,
            order,
            order,
          ),
        ),
      )
    }.collectAsLazyPagingItems()
  val orderDto = order.toOrderDto()

  val user =
    User(
      userId = 0,
      subject = "Subject",
      password = "Password",
      image = "Image",
      firstName = "FirstName",
      lastName = "LastName",
      email = "Email",
      profile = UserProfile.Other,
      salt = "Salt",
    )

  AppTheme {
    OrderScreen(
      user = null,
      onUser = {},
      orders = orders,
      loginResult = LoginResult.Success,
      onLoginResult = {},
      onAddNewUserLocally = { _ -> },
      onDeleteOrderLocally = { _ -> },
      onAddNewOrderLocally = { _ -> },
      onEditOrderLocally = { _ -> },
      onLogin = { _, _ -> Result.success(TokenDto("")) },
      onGetUserRemotely = { _, _ -> Result.success(user.toUserDto()) },
      onAddNewOrderRemotely = { _ -> Result.success(orderDto) },
      onDeleteOrderRemotely = { _, _ -> false },
      onEditOrderRemotely = { _, _, _, _ -> false },
      onEncryptToken = {},
      onDecryptSubjectAndPassword = { Pair("", "") },
      onDecryptToken = { "" },
      networkStatus = NetworkStatus.Available,
    )
  }
}