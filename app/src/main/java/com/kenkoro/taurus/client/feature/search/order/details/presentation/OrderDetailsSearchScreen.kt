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
import com.kenkoro.taurus.client.feature.search.order.details.presentation.util.OrderDetailsSearchScreenShared
import com.kenkoro.taurus.client.ui.theme.AppTheme

@Composable
fun OrderDetailsSearchScreen(
  modifier: Modifier = Modifier,
  navigator: OrderDetailsSearchScreenNavigator,
  shared: OrderDetailsSearchScreenShared,
) {
  AppTheme {
    Scaffold(
      modifier =
        modifier
          .statusBarsPadding()
          .navigationBarsPadding(),
      topBar = { OrderDetailsTopBar(navigator = navigator, state = shared.selectedDropDownState) },
      content = { paddingValues ->
        Surface(
          modifier =
            modifier
              .fillMaxSize()
              .background(MaterialTheme.colorScheme.background)
              .padding(paddingValues),
        ) {
          OrderDetailsSearchContent(
            navigator = navigator,
            shared = shared,
          )
        }
      },
    )
  }
}