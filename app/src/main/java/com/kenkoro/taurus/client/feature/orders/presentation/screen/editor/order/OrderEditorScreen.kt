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
  AppTheme {
    Scaffold(
      modifier =
        Modifier
          .statusBarsPadding()
          .navigationBarsPadding(),
      topBar = {
        OrderEditorTopBar(
          userId = userId,
          userSubject = userSubject,
          orderStatus = orderStatus,
          editOrder = editOrder,
          orderStatesHolder = orderStatesHolder,
          onNavUp = onNavUp,
          onAddNewOrderRemotely = onAddNewOrderRemotely,
          onEditOrderRemotely = onEditOrderRemotely,
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