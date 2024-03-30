package com.kenkoro.taurus.client.feature.sewing.presentation.screen.order

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.kenkoro.taurus.client.core.connectivity.Status
import com.kenkoro.taurus.client.core.local.LocalContentHeight
import com.kenkoro.taurus.client.core.local.LocalContentWidth
import com.kenkoro.taurus.client.core.local.LocalShape
import com.kenkoro.taurus.client.feature.sewing.domain.model.Order
import com.kenkoro.taurus.client.feature.sewing.domain.model.User
import com.kenkoro.taurus.client.feature.sewing.presentation.screen.order.components.OrderContent
import com.kenkoro.taurus.client.feature.sewing.presentation.shared.components.ErrorSnackbar
import com.kenkoro.taurus.client.ui.theme.AppTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun OrderScreen(
  orders: LazyPagingItems<Order>,
  user: User?,
  networkStatus: Status,
) {
  val shape = LocalShape.current
  val contentHeight = LocalContentHeight.current
  val contentWidth = LocalContentWidth.current

  val snackbarHostState = remember { SnackbarHostState() }

  AppTheme {
    Scaffold(
      snackbarHost = {
        SnackbarHost(hostState = snackbarHostState) {
          ErrorSnackbar(
            modifier = Modifier.padding(bottom = 20.dp),
            snackbarData = it,
          )
        }
      },
      bottomBar = {
        Column(
          modifier =
            Modifier
              .fillMaxWidth()
              .background(MaterialTheme.colorScheme.background),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center,
        ) {
          Spacer(modifier = Modifier.height(contentHeight.medium))
          Button(
            enabled = networkStatus == Status.Available,
            modifier =
              Modifier
                .size(contentWidth.standard + 30.dp, contentHeight.halfStandard),
            shape = RoundedCornerShape(shape.small),
            onClick = { /*TODO*/ },
          ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add a new order")
          }
          Spacer(modifier = Modifier.height(contentHeight.medium))
        }
      },
    ) {
      Surface(
        modifier =
          Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
      ) {
        OrderContent(
          orders = orders,
          snackbarHostState = snackbarHostState,
          user = user,
          networkStatus = networkStatus,
        )
      }
    }
  }
}