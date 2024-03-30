package com.kenkoro.taurus.client.feature.sewing.presentation.screen.order

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.kenkoro.taurus.client.R
import com.kenkoro.taurus.client.core.connectivity.Status
import com.kenkoro.taurus.client.core.local.LocalContentHeight
import com.kenkoro.taurus.client.core.local.LocalContentWidth
import com.kenkoro.taurus.client.core.local.LocalShape
import com.kenkoro.taurus.client.feature.sewing.data.util.UserProfile
import com.kenkoro.taurus.client.feature.sewing.domain.model.User
import com.kenkoro.taurus.client.feature.sewing.presentation.screen.order.components.OrderContent
import com.kenkoro.taurus.client.feature.sewing.presentation.shared.components.ErrorSnackbar
import com.kenkoro.taurus.client.ui.theme.AppTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun OrderScreen(
  orderViewModel: OrderViewModel = hiltViewModel(),
  user: User?,
  networkStatus: Status,
) {
  val shape = LocalShape.current
  val contentWidth = LocalContentWidth.current
  val contentHeight = LocalContentHeight.current

  val snackbarHostState = remember { SnackbarHostState() }
  val orders = orderViewModel.orderPagingFlow.collectAsLazyPagingItems()

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
      floatingActionButton = {
        if (user != null) {
          AnimatedVisibility(visible = user.profile == UserProfile.Customer) {
            Button(
              enabled = networkStatus == Status.Available,
              modifier =
                Modifier
                  .size(contentWidth.halfStandard, contentHeight.standard),
              shape = RoundedCornerShape(shape.medium),
              onClick = { /*TODO*/ },
            ) {
              Row {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Create a new order")
                Spacer(modifier = Modifier.width(contentWidth.small))
                Text(text = stringResource(id = R.string.new_order_button))
              }
            }
          }
        }
      },
    ) {
      Surface(
        modifier =
          Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
      ) {
        OrderContent(orders, snackbarHostState, user)
      }
    }
  }
}