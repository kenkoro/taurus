package com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kenkoro.taurus.client.feature.orders.domain.NewOrder
import com.kenkoro.taurus.client.feature.orders.domain.OrderStatus
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.composables.OrderEditorContent
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.composables.bars.OrderEditorTopBar
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.util.OrderEditorScreenNavigator
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.util.OrderEditorScreenUtils
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.util.OrderStatesHolder
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.util.OrderScreenRemoteHandler
import com.kenkoro.taurus.client.ui.theme.AppTheme

@Composable
fun OrderEditorScreen(
  modifier: Modifier = Modifier,
  remoteHandler: OrderScreenRemoteHandler,
  navigator: OrderEditorScreenNavigator,
  utils: OrderEditorScreenUtils,
  states: OrderStatesHolder,
) {
  val user = utils.user
  val editOrder = utils.editOrder

  val validateChanges = {
    states.customerState.isValid &&
      states.titleState.isValid &&
      states.modelState.isValid &&
      states.sizeState.isValid &&
      states.colorState.isValid &&
      states.categoryState.isValid &&
      states.quantityState.isValid
  }

  val saveChanges =
    suspend {
      val newOrder =
        NewOrder(
          orderId = states.orderIdState,
          customer = states.customerState.text,
          date = System.currentTimeMillis(),
          title = states.titleState.text,
          model = states.modelState.text,
          size = states.sizeState.text,
          color = states.colorState.text,
          category = states.categoryState.text,
          quantity = states.quantityState.text.toIntOrNull() ?: 0,
          status =
            if (!editOrder) {
              OrderStatus.Idle
            } else {
              utils.orderStatus
            },
          creatorId = user?.userId ?: 0,
        )

      if (editOrder) {
        remoteHandler.editOrder(newOrder, user?.subject ?: "")
      } else {
        remoteHandler.addNewOrder(newOrder).isSuccess
      }
    }

  AppTheme {
    Scaffold(
      modifier =
        Modifier
          .statusBarsPadding()
          .navigationBarsPadding(),
      topBar = {
        OrderEditorTopBar(
          editOrder = editOrder,
          orderStatesHolder = states,
          onNavUp = navigator.navUp,
          saveChanges = saveChanges,
          validateChanges = validateChanges,
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
            networkStatus = networkStatus,
            orderStatesHolder = states,
            onNavUp = onNavUp,
            saveChanges = saveChanges,
            validateChanges = validateChanges,
          )
        }
      },
    )
  }
}