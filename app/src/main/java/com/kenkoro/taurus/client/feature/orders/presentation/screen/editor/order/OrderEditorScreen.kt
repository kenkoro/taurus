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
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.composables.OrderEditorContent
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.composables.bars.OrderEditorTopBar
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.composables.bars.util.OrderEditorScreenExtras
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.util.OrderEditorScreenNavigator
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.util.OrderEditorScreenUtils
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.util.OrderStatesHolder
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.util.OrderScreenRemoteHandler
import com.kenkoro.taurus.client.feature.shared.states.TaurusTextFieldState
import com.kenkoro.taurus.client.ui.theme.AppTheme

@Composable
fun OrderEditorScreen(
  modifier: Modifier = Modifier,
  remoteHandler: OrderScreenRemoteHandler,
  navigator: OrderEditorScreenNavigator,
  utils: OrderEditorScreenUtils,
  states: OrderStatesHolder,
  onStateChangeOrderDetailsSearchBehavior: (TaurusTextFieldState) -> Unit = {},
) {
  val user = utils.user
  val editOrder = utils.editOrder

  val extras =
    OrderEditorScreenExtras(
      saveChanges = { false },
      validateChanges = { false },
    )

  AppTheme {
    Scaffold(
      modifier =
        Modifier
          .statusBarsPadding()
          .navigationBarsPadding(),
      topBar = {
        OrderEditorTopBar(
          editOrder = editOrder,
          states = states,
          navigator = navigator,
          extras = extras,
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
            extras = extras,
            onStateChangeOrderDetailsSearchBehavior = onStateChangeOrderDetailsSearchBehavior,
          )
        }
      },
    )
  }
}