package com.kenkoro.taurus.client.feature.sewing.presentation.screen.order.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.kenkoro.taurus.client.core.local.LocalArrangement
import com.kenkoro.taurus.client.di.AppModule
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.request.Order
import com.kenkoro.taurus.client.ui.theme.AppTheme

@Composable
fun OrderContent(orders: LazyPagingItems<Order>) {
  val arrangement = LocalArrangement.current

  Column(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.spacedBy(arrangement.standard)
  ) {
    OrderTopBar()
    LazyColumn(modifier = Modifier.fillMaxSize()) {
      items(
        count = orders.itemCount,
        key = orders.itemKey { it.orderId }
      ) { index ->
        val order = orders[index]
        if (order != null) {
          Text(text = order.toString())
        }
      }
      item {
        if (orders.loadState.append is LoadState.Loading) {
          CircularProgressIndicator()
        }
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
private fun OrderContentPrev() {
  val orders = AppModule.provideOrderPager(
    AppModule.provideOrderRepository()
  ).flow.collectAsLazyPagingItems()
  AppTheme {
    Surface(
      modifier =
      Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background),
    ) {
      OrderContent(orders = orders)
    }
  }
}