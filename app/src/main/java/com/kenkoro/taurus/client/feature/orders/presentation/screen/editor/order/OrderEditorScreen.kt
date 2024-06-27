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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.kenkoro.taurus.client.R
import com.kenkoro.taurus.client.core.connectivity.NetworkStatus
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.composables.OrderEditorContent
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.composables.bars.OrderEditorTopBar
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.util.OrderStatesHolder
import com.kenkoro.taurus.client.ui.theme.AppTheme

@Composable
fun OrderEditorScreen(
  modifier: Modifier = Modifier,
  networkStatus: NetworkStatus,
  orderStatesHolder: OrderStatesHolder = OrderStatesHolder(),
  editOrder: Boolean = false,
  onNavUp: () -> Unit = {},
  onSaveChanges: () -> Unit = {},
) {
  AppTheme {
    Scaffold(
      modifier =
        Modifier
          .statusBarsPadding()
          .navigationBarsPadding(),
      topBar = {
        OrderEditorTopBar(
          label =
            if (editOrder) {
              stringResource(id = R.string.edit_order_label)
            } else {
              stringResource(id = R.string.create_order_label)
            },
          onNavUp = onNavUp,
          onSaveChanges = onSaveChanges,
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
  AppTheme {
    OrderEditorScreen(
      networkStatus = NetworkStatus.Available,
    )
  }
}