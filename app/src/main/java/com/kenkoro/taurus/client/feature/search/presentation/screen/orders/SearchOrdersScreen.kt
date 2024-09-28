package com.kenkoro.taurus.client.feature.search.presentation.screen.orders

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.kenkoro.taurus.client.R
import com.kenkoro.taurus.client.feature.search.presentation.screen.orders.util.SearchOrdersScreenShared
import com.kenkoro.taurus.client.feature.shared.components.BottomNavBar

@Composable
fun SearchOrdersScreen(
  modifier: Modifier = Modifier,
  shared: SearchOrdersScreenShared,
) {
  Scaffold(
    modifier =
      modifier
        .statusBarsPadding()
        .navigationBarsPadding(),
    bottomBar = {
      if (shared.user != null) {
        BottomNavBar(
          items = shared.items,
          currentRoute = shared.currentRoute,
        )
      }
    },
  ) { innerPaddings ->
    Box(
      modifier =
        Modifier
          .fillMaxSize()
          .padding(innerPaddings),
      contentAlignment = Alignment.Center,
    ) {
      Text(text = stringResource(id = R.string.search_orders_decription_title))
    }
  }
}