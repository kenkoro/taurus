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
import com.kenkoro.taurus.client.R
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.util.OrderStatesHolder
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.components.OrderContent
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.components.bars.OrderBottomBar
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.components.bars.OrderTopBar
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.components.bars.util.OrderScreenExtras
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.util.OrderScreenLocalHandler
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.util.OrderScreenNavigator
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.util.OrderScreenRemoteHandler
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.util.OrderScreenSnackbarsHolder
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.util.OrderScreenUtils
import com.kenkoro.taurus.client.feature.profile.domain.UserProfile.Customer
import com.kenkoro.taurus.client.feature.shared.components.TaurusSnackbar
import com.kenkoro.taurus.client.ui.theme.AppTheme

@Composable
fun OrderScreen(
  modifier: Modifier = Modifier,
  localHandler: OrderScreenLocalHandler = OrderScreenLocalHandler(),
  remoteHandler: OrderScreenRemoteHandler,
  navigator: OrderScreenNavigator,
  utils: OrderScreenUtils,
  states: OrderStatesHolder,
) {
  val snackbarHostState = remember { SnackbarHostState() }
  val errorSnackbarHostState = remember { SnackbarHostState() }
  val internetErrorSnackbarHostState = remember { SnackbarHostState() }
  val lazyOrdersState = rememberLazyListState()

  val notImplementedYetMessage = stringResource(id = R.string.not_implemented_yet)
  val loginErrorMessage = stringResource(id = R.string.login_fail)
  val internetConnectionErrorMessage = stringResource(id = R.string.check_internet_connection)
  val paginatedOrdersErrorMessage = stringResource(id = R.string.paginated_orders_error)
  val orderAccessErrorMessage = stringResource(id = R.string.orders_access_error)
  val apiRequestErrorMessage = stringResource(id = R.string.request_error)
  val orderWasDeletedMessage = stringResource(id = R.string.order_was_deleted)

  val okActionLabel = stringResource(id = R.string.ok)

  val snackbarsHolder =
    OrderScreenSnackbarsHolder(
      notImplementedError = {
        snackbarHostState.showSnackbar(
          message = notImplementedYetMessage,
          actionLabel = okActionLabel,
          duration = SnackbarDuration.Short,
        )
      },
      loginError = {
        errorSnackbarHostState.showSnackbar(
          message = loginErrorMessage,
          actionLabel = okActionLabel,
          duration = SnackbarDuration.Long,
        )
      },
      internetConnectionError = {
        internetErrorSnackbarHostState.showSnackbar(
          message = internetConnectionErrorMessage,
          duration = SnackbarDuration.Indefinite,
        )
      },
      getPaginatedOrdersError = {
        errorSnackbarHostState.showSnackbar(
          message = paginatedOrdersErrorMessage,
          actionLabel = okActionLabel,
          duration = SnackbarDuration.Long,
        )
      },
      accessToOrdersError = {
        errorSnackbarHostState.showSnackbar(
          message = orderAccessErrorMessage,
          actionLabel = okActionLabel,
          duration = SnackbarDuration.Long,
        )
      },
      apiError = {
        errorSnackbarHostState.showSnackbar(
          message = apiRequestErrorMessage,
          actionLabel = okActionLabel,
          duration = SnackbarDuration.Long,
        )
      },
      orderWasDeleted = { orderId ->
        snackbarHostState.showSnackbar(
          message = "$orderWasDeletedMessage $orderId",
          actionLabel = okActionLabel,
          duration = SnackbarDuration.Short,
        )
      },
    )
  val orderScreenExtras =
    OrderScreenExtras(
      isScrolling = lazyOrdersState.isScrollInProgress,
      username = utils.user?.firstName,
    )

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
          snackbarHostState = internetErrorSnackbarHostState,
          onDismiss = { internetErrorSnackbarHostState.currentSnackbarData?.dismiss() },
          containerColor = MaterialTheme.colorScheme.errorContainer,
          contentColor = MaterialTheme.colorScheme.onErrorContainer,
          centeredContent = true,
        )
      },
      topBar = {
        OrderTopBar(
          utils = utils,
          snackbarsHolder = snackbarsHolder,
          navigator = navigator,
          extras = orderScreenExtras,
        )
      },
      bottomBar = {
        if (utils.user != null && utils.user.profile == Customer) {
          OrderBottomBar(
            utils = utils,
            navigator = navigator,
            extras = orderScreenExtras,
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
            localHandler = localHandler,
            remoteHandler = remoteHandler,
            navigator = navigator,
            utils = utils,
            statesHolder = states,
            snackbarsHolder = snackbarsHolder,
          )
        }
      },
    )
  }
}