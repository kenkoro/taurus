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
import androidx.compose.ui.tooling.preview.Preview
import com.kenkoro.taurus.client.core.connectivity.NetworkStatus
import com.kenkoro.taurus.client.feature.orders.data.remote.dto.OrderDto
import com.kenkoro.taurus.client.feature.orders.domain.NewOrder
import com.kenkoro.taurus.client.feature.orders.domain.OrderStatus
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.composables.OrderEditorContent
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.composables.bars.OrderEditorTopBar
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.util.OrderStatesHolder
import com.kenkoro.taurus.client.ui.theme.AppTheme

@Composable
fun OrderEditorScreen(
  modifier: Modifier = Modifier,
  userId: Int = 0,
  userSubject: String = "",
  orderStatus: OrderStatus = OrderStatus.Idle,
  networkStatus: NetworkStatus,
  orderStatesHolder: OrderStatesHolder = OrderStatesHolder(),
  editOrder: Boolean = false,
  onNavUp: () -> Unit = {},
  onAddNewOrderRemotely: suspend (NewOrder) -> Result<OrderDto>,
  onEditOrderRemotely: suspend (NewOrder, Int, String) -> Boolean = { _, _, _ -> false },
) {
  val validateChanges = {
    orderStatesHolder.customerState.isValid &&
      orderStatesHolder.titleState.isValid &&
      orderStatesHolder.modelState.isValid &&
      orderStatesHolder.sizeState.isValid &&
      orderStatesHolder.colorState.isValid &&
      orderStatesHolder.categoryState.isValid &&
      orderStatesHolder.quantityState.isValid
  }

  val saveChanges =
    suspend {
      val newOrder =
        NewOrder(
          customer = orderStatesHolder.customerState.text,
          date = System.currentTimeMillis(),
          title = orderStatesHolder.titleState.text,
          model = orderStatesHolder.modelState.text,
          size = orderStatesHolder.sizeState.text,
          color = orderStatesHolder.colorState.text,
          category = orderStatesHolder.categoryState.text,
          quantity = orderStatesHolder.quantityState.text.toIntOrNull() ?: 0,
          status =
            if (!editOrder) {
              OrderStatus.Idle
            } else {
              orderStatus
            },
          creatorId = userId,
        )

      if (editOrder) {
        onEditOrderRemotely(newOrder, orderStatesHolder.orderIdState, userSubject)
      } else {
        onAddNewOrderRemotely(newOrder).isSuccess
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
          orderStatesHolder = orderStatesHolder,
          onNavUp = onNavUp,
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
            orderStatesHolder = orderStatesHolder,
            onNavUp = onNavUp,
            saveChanges = saveChanges,
            validateChanges = validateChanges,
          )
        }
      },
    )
  }
}

@Preview
@Composable
private fun OrderEditorScreenPrev() {
  val orderDto =
    OrderDto(
      recordId = 0,
      orderId = 0,
      customer = "",
      date = 0L,
      title = "",
      model = "",
      size = "",
      color = "",
      category = "",
      quantity = 0,
      status = OrderStatus.Idle,
      creatorId = 0,
    )

  AppTheme {
    OrderEditorScreen(
      networkStatus = NetworkStatus.Available,
      onAddNewOrderRemotely = { Result.success(orderDto) },
    )
  }
}