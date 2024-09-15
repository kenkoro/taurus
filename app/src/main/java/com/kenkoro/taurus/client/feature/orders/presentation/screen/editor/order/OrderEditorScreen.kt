package com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order

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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.kenkoro.taurus.client.R
import com.kenkoro.taurus.client.feature.orders.domain.OrderStatus
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.composables.OrderEditorContent
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.composables.bars.OrderEditorTopBar
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.composables.bars.util.OrderEditorScreenExtras
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.states.OrderStatesHolder
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.util.OrderEditorScreenNavigator
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.util.OrderEditorScreenSnackbarsHolder
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.util.OrderEditorScreenUtils
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.util.OrderScreenRemoteHandler
import com.kenkoro.taurus.client.feature.shared.components.TaurusSnackbar
import com.kenkoro.taurus.client.ui.theme.AppTheme

@Composable
fun OrderEditorScreen(
  modifier: Modifier = Modifier,
  remoteHandler: OrderScreenRemoteHandler,
  navigator: OrderEditorScreenNavigator,
  utils: OrderEditorScreenUtils,
  states: OrderStatesHolder,
) {
  val errorSnackbarHostState = remember { SnackbarHostState() }
  val user = utils.user
  val editOrder = utils.editOrder

  val failedOrderEditorValidationMessage =
    stringResource(id = R.string.failed_order_editor_validation)
  val apiRequestErrorMessage = stringResource(id = R.string.request_error)

  val okActionLabel = stringResource(id = R.string.ok)

  val extras =
    OrderEditorScreenExtras(
      validateChanges = {
        states.customerState.isValid &&
          states.titleState.isValid &&
          states.modelState.isValid &&
          states.sizeState.isValid &&
          states.colorState.isValid &&
          states.categoryState.isValid
      },
      saveChanges = {
        if (editOrder) {
          val editedOrder = states.packEditOrder(utils.orderStatus, user?.userId ?: 0)
          remoteHandler.editOrder(editedOrder, user?.subject ?: "")
        } else {
          val newOrder = states.packNewOrder(OrderStatus.Idle, user?.userId ?: 0)
          Log.d("kenkoro", newOrder.toString())
          remoteHandler.addNewOrder(newOrder).isSuccess
        }
      },
    )

  val snackbarsHolder =
    OrderEditorScreenSnackbarsHolder(
      failedOrderEditorValidation = {
        errorSnackbarHostState.showSnackbar(
          message = failedOrderEditorValidationMessage,
          actionLabel = okActionLabel,
          duration = SnackbarDuration.Short,
        )
      },
      apiError = {
        errorSnackbarHostState.showSnackbar(
          message = apiRequestErrorMessage,
          actionLabel = okActionLabel,
          duration = SnackbarDuration.Long,
        )
      },
    )

  AppTheme {
    Scaffold(
      modifier =
        Modifier
          .statusBarsPadding()
          .navigationBarsPadding(),
      snackbarHost = {
        TaurusSnackbar(
          snackbarHostState = errorSnackbarHostState,
          onDismiss = { errorSnackbarHostState.currentSnackbarData?.dismiss() },
          containerColor = MaterialTheme.colorScheme.errorContainer,
          contentColor = MaterialTheme.colorScheme.onErrorContainer,
        )
      },
      topBar = {
        OrderEditorTopBar(
          editOrder = editOrder,
          states = states,
          navigator = navigator,
          extras = extras,
          snackbarsHolder = snackbarsHolder,
        )
      },
      content = { paddingValues ->
        Surface(
          modifier =
            modifier
              .fillMaxSize()
              .background(MaterialTheme.colorScheme.background)
              .padding(paddingValues),
        ) {
          OrderEditorContent(
            networkStatus = utils.network,
            states = states,
            navigator = navigator,
            onStateChangeOrderDetailsSearchBehavior = utils.changeOrderDetailsSearchScreenBehavior,
          )
        }
      },
    )
  }
}