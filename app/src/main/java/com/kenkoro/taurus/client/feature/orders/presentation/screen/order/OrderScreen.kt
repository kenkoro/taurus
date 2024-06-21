package com.kenkoro.taurus.client.feature.orders.presentation.screen.order

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.PagingData
import com.kenkoro.taurus.client.core.connectivity.NetworkStatus
import com.kenkoro.taurus.client.feature.login.data.mappers.toUserDto
import com.kenkoro.taurus.client.feature.orders.data.mappers.toOrderDto
import com.kenkoro.taurus.client.feature.orders.domain.Order
import com.kenkoro.taurus.client.feature.orders.domain.OrderStatus
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.components.OrderContent
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.components.bars.OrderBottomBar
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.components.bars.OrderTopBar
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.handlers.LocalHandler
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.handlers.RemoteHandler
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.states.LoginState
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.util.OrderFilterStrategy
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.util.SnackbarsHolder
import com.kenkoro.taurus.client.feature.profile.domain.User
import com.kenkoro.taurus.client.feature.profile.domain.UserProfile.Customer
import com.kenkoro.taurus.client.feature.profile.domain.UserProfile.Other
import com.kenkoro.taurus.client.feature.shared.components.TaurusSnackbar
import com.kenkoro.taurus.client.feature.shared.data.remote.dto.TokenDto
import com.kenkoro.taurus.client.ui.theme.AppTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@Composable
fun OrderScreen(
  modifier: Modifier = Modifier,
  ordersPagingFlow: Flow<PagingData<Order>>,
  user: User? = null,
  loginState: LoginState,
  networkStatus: NetworkStatus,
  selectedOrderRecordId: Int? = null,
  onUser: (User) -> Unit = {},
  onFilterStrategy: (OrderFilterStrategy?) -> Unit = {},
  onSelectOrder: (Int?) -> Unit = {},
  onLoginState: (LoginState) -> Unit = {},
  localHandler: LocalHandler = LocalHandler(),
  remoteHandler: RemoteHandler,
  onEncryptToken: (String) -> Unit = {},
  onDecryptSubjectAndPassword: () -> Pair<String, String>,
  onDecryptToken: () -> String,
  onNavigateToProfileScreen: () -> Unit = {},
) {
  val snackbarHostState = remember { SnackbarHostState() }
  val errorSnackbarHostState = remember { SnackbarHostState() }
  val internetSnackbarHostState = remember { SnackbarHostState() }
  val snackbarsHolder =
    remember {
      SnackbarsHolder(
        snackbarHostState = snackbarHostState,
        errorSnackbarHostState = errorSnackbarHostState,
        internetErrorSnackbarHostState = internetSnackbarHostState,
      )
    }
  val lazyOrdersState = rememberLazyListState()

  AppTheme {
    Scaffold(
      modifier =
        Modifier
          .statusBarsPadding()
          .navigationBarsPadding(),
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
      topBar = {
        OrderTopBar(
          networkStatus = networkStatus,
          isScrollingInProgress = lazyOrdersState.isScrollInProgress,
          userName = user?.firstName,
          snackbarsHolder = snackbarsHolder,
          onNavigateToProfileScreen = onNavigateToProfileScreen,
        )
      },
      bottomBar = {
        if (user != null && user.profile == Customer) {
          OrderBottomBar(
            networkStatus = networkStatus,
            isScrollingInProgress = lazyOrdersState.isScrollInProgress,
            snackbarsHolder = snackbarsHolder,
          )
        }
      },
      content = { paddingValues ->
        Surface(
          modifier =
            modifier
              .fillMaxSize()
              .background(MaterialTheme.colorScheme.background)
              .padding(paddingValues),
        ) {
          OrderContent(
            ordersPagingFlow = ordersPagingFlow,
            networkStatus = networkStatus,
            user = user,
            onUser = onUser,
            loginState = loginState,
            lazyOrdersState = lazyOrdersState,
            selectedOrderRecordId = selectedOrderRecordId,
            onFilterStrategy = onFilterStrategy,
            onSelectOrder = onSelectOrder,
            onLoginState = onLoginState,
            localHandler = localHandler,
            remoteHandler = remoteHandler,
            snackbarsHolder = snackbarsHolder,
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
  val remoteHandler =
    RemoteHandler(
      login = { _, _ -> Result.success(TokenDto("")) },
      getUser = { _, _ -> Result.success(user.toUserDto()) },
      addNewOrder = { _ -> Result.success(orderDto) },
    )

  AppTheme {
    OrderScreen(
      ordersPagingFlow = ordersFlow,
      user = user,
      loginState = LoginState.NotLoggedYet,
      networkStatus = NetworkStatus.Unavailable,
      selectedOrderRecordId = null,
      remoteHandler = remoteHandler,
      onDecryptSubjectAndPassword = { Pair("", "") },
      onDecryptToken = { "" },
    )
  }
}