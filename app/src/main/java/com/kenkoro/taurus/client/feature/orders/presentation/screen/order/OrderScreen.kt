package com.kenkoro.taurus.client.feature.orders.presentation.screen.order

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.rememberLazyListState
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
import com.kenkoro.taurus.client.R
import com.kenkoro.taurus.client.core.connectivity.NetworkStatus
import com.kenkoro.taurus.client.feature.login.data.local.UserEntity
import com.kenkoro.taurus.client.feature.login.data.mappers.toUserDto
import com.kenkoro.taurus.client.feature.orders.data.mappers.toOrderDto
import com.kenkoro.taurus.client.feature.orders.data.remote.dto.NewOrderDto
import com.kenkoro.taurus.client.feature.orders.data.remote.dto.OrderDto
import com.kenkoro.taurus.client.feature.orders.domain.NewOrder
import com.kenkoro.taurus.client.feature.orders.domain.Order
import com.kenkoro.taurus.client.feature.orders.domain.OrderStatus
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.components.OrderContent
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.components.bars.OrderBottomBar
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.components.bars.OrderTopBar
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.util.LoginState
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.util.OrderFilterStrategy
import com.kenkoro.taurus.client.feature.profile.data.remote.dto.UserDto
import com.kenkoro.taurus.client.feature.profile.domain.UserProfile.Customer
import com.kenkoro.taurus.client.feature.profile.domain.UserProfile.Other
import com.kenkoro.taurus.client.feature.sewing.domain.model.User
import com.kenkoro.taurus.client.feature.shared.components.TaurusSnackbar
import com.kenkoro.taurus.client.feature.shared.data.remote.dto.TokenDto
import com.kenkoro.taurus.client.ui.theme.AppTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@Composable
fun OrderScreen(
  user: User?,
  onUser: (User) -> Unit,
  loginState: LoginState,
  networkStatus: NetworkStatus,
  selectedOrderRecordId: Int? = null,
  ordersPagingFlow: Flow<PagingData<Order>>,
  onStrategy: (OrderFilterStrategy?) -> Unit = {},
  onSelectOrder: (Int?) -> Unit = {},
  onLoginState: (LoginState) -> Unit,
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
  onNavigateToProfileScreen: () -> Unit,
) {
  val snackbarHostState = remember { SnackbarHostState() }
  val errorSnackbarHostState = remember { SnackbarHostState() }
  val internetSnackbarHostState = remember { SnackbarHostState() }

  val internetConnectionErrorMessage = stringResource(id = R.string.check_internet_connection)
  val orderWasDeletedMessage = stringResource(id = R.string.order_was_deleted)
  val paginatedOrdersErrorMessage = stringResource(id = R.string.paginated_orders_error)
  val loginErrorMessage = stringResource(id = R.string.login_fail)
  val notImplementedYetMessage = stringResource(id = R.string.not_implemented_yet)
  val orderAccessErrorMessage = stringResource(id = R.string.orders_access_error)

  val cancelOrderDeletionLabel = stringResource(id = R.string.cancel)
  val okActionLabel = stringResource(id = R.string.ok)

  val lazyOrdersState = rememberLazyListState()

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
          isScrollingInProgress = lazyOrdersState.isScrollInProgress,
          userName = user?.firstName,
          onSortOrdersShowSnackbar = {
            snackbarHostState.showSnackbar(
              message = notImplementedYetMessage,
              actionLabel = okActionLabel,
            )
          },
          onFilterOrdersShowSnackbar = {
            snackbarHostState.showSnackbar(
              message = notImplementedYetMessage,
              actionLabel = okActionLabel,
            )
          },
          onNavigateToProfileScreen = onNavigateToProfileScreen,
        )
      },
      bottomBar = {
        // TODO: onAddNewOrderRemotely callback
        val userProfile = user?.profile ?: Other
        if (userProfile == Customer) {
          OrderBottomBar(
            networkStatus = networkStatus,
            isScrollingInProgress = lazyOrdersState.isScrollInProgress,
            onAddNewOrderShowSnackbar = {
              snackbarHostState.showSnackbar(
                message = notImplementedYetMessage,
                actionLabel = okActionLabel,
              )
            },
          )
        }
      },
      content = { paddingValues ->
        Surface(
          modifier =
            Modifier
              .fillMaxSize()
              .background(MaterialTheme.colorScheme.background)
              .padding(paddingValues),
        ) {
          OrderContent(
            networkStatus = networkStatus,
            user = user,
            onUser = onUser,
            loginState = loginState,
            lazyOrdersState = lazyOrdersState,
            ordersPagingFlow = ordersPagingFlow,
            selectedOrderRecordId = selectedOrderRecordId,
            onStrategy = onStrategy,
            onSelectOrder = onSelectOrder,
            onLoginState = onLoginState,
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
            onOrderAccessErrorShowSnackbar = {
              errorSnackbarHostState.showSnackbar(
                message = orderAccessErrorMessage,
                actionLabel = okActionLabel,
              )
            },
            onEncryptToken = onEncryptToken,
            onDecryptSubjectAndPassword = onDecryptSubjectAndPassword,
            onDecryptToken = onDecryptToken,
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
  val ordersFlow =
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
    }
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
      profile = Other,
      salt = "Salt",
    )

  AppTheme {
    OrderScreen(
      user = null,
      onUser = {},
      loginState = LoginState.Success,
      ordersPagingFlow = ordersFlow,
      onLoginState = {},
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
      onNavigateToProfileScreen = {},
      networkStatus = NetworkStatus.Available,
    )
  }
}