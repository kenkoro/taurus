package com.kenkoro.taurus.client.feature.search.order.details.presentation

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
import com.kenkoro.taurus.client.feature.search.order.details.presentation.composables.OrderDetailsSearchContent
import com.kenkoro.taurus.client.feature.search.order.details.presentation.composables.bars.OrderDetailsTopBar
import com.kenkoro.taurus.client.feature.search.order.details.presentation.util.OrderDetailsSearchScreenNavigator
import com.kenkoro.taurus.client.feature.search.order.details.presentation.util.OrderDetailsSearchScreenRemoteHandler
import com.kenkoro.taurus.client.feature.search.order.details.presentation.util.OrderDetailsSearchScreenUtils
import com.kenkoro.taurus.client.ui.theme.AppTheme

@Composable
fun OrderDetailsSearchScreen(
  modifier: Modifier = Modifier,
  remoteHandler: OrderDetailsSearchScreenRemoteHandler,
  navigator: OrderDetailsSearchScreenNavigator,
  utils: OrderDetailsSearchScreenUtils,
) {
  AppTheme {
    Scaffold(
      modifier =
        modifier
          .statusBarsPadding()
          .navigationBarsPadding(),
      topBar = { OrderDetailsTopBar(navigator = navigator, state = utils.selectedSearchState) },
      content = { paddingValues ->
        Surface(
          modifier =
            modifier
              .fillMaxSize()
              .background(MaterialTheme.colorScheme.background)
              .padding(paddingValues),
        ) {
          OrderDetailsSearchContent(
            remoteHandler = remoteHandler,
            navigator = navigator,
            state = utils.selectedSearchState,
          )
        }
      },
    )
  }
}