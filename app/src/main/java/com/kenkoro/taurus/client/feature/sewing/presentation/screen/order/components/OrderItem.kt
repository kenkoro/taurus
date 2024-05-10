package com.kenkoro.taurus.client.feature.sewing.presentation.screen.order.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import com.kenkoro.taurus.client.core.connectivity.NetworkStatus
import com.kenkoro.taurus.client.core.local.LocalContentHeight
import com.kenkoro.taurus.client.core.local.LocalContentWidth
import com.kenkoro.taurus.client.core.local.LocalShape
import com.kenkoro.taurus.client.feature.sewing.domain.model.NewOrder
import com.kenkoro.taurus.client.feature.sewing.domain.model.Order
import com.kenkoro.taurus.client.feature.sewing.domain.model.enums.OrderStatus
import com.kenkoro.taurus.client.ui.theme.AppTheme

@Composable
fun OrderItem(
  order: Order,
  onAddNewOrderLocally: suspend (NewOrder) -> Unit,
  onDeleteOrderLocally: suspend (Order) -> Unit,
  onDeleteOrderRemotely: suspend (orderId: Int, deleterSubject: String) -> Boolean,
  onDeleteOrderShowSnackbar: suspend () -> SnackbarResult,
  networkStatus: NetworkStatus,
  modifier: Modifier = Modifier,
) {
  val shape = LocalShape.current
  val contentWidth = LocalContentWidth.current
  val contentHeight = LocalContentHeight.current

  var clicked by rememberSaveable {
    mutableStateOf(false)
  }
  var visible by rememberSaveable {
    mutableStateOf(true)
  }
  val heightAnimated by animateDpAsState(
    targetValue =
      if (clicked) contentHeight.orderItemExpanded else contentHeight.orderItemNotExpanded,
    label = "AnimatedHeightOfOrderItem",
    animationSpec = tween(300),
  )

  AnimatedVisibility(visible = visible) {
    Column {
      Spacer(modifier = Modifier.height(contentHeight.medium))
      Column(
        modifier =
          Modifier
            .width(contentWidth.orderItem)
            .height(heightAnimated)
            .clip(RoundedCornerShape(shape.medium))
            .background(MaterialTheme.colorScheme.primaryContainer)
            .clickable { clicked = !clicked },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(contentHeight.large),
      ) {
        Column(
          modifier =
            Modifier
              .fillMaxWidth()
              .fillMaxHeight(.7F),
          verticalArrangement = Arrangement.Top,
          horizontalAlignment = Alignment.CenterHorizontally,
        ) {
          Spacer(modifier = Modifier.height(contentHeight.medium))
          OrderItemContent(
            order = order,
            clicked = clicked,
          )
        }
        Column(
          modifier = Modifier.fillMaxSize(),
          verticalArrangement = Arrangement.Bottom,
          horizontalAlignment = Alignment.CenterHorizontally,
        ) {
          OrderItemButton(
            order = order,
            onAddNewOrderLocally = onAddNewOrderLocally,
            onDeleteOrderLocally = onDeleteOrderLocally,
            onDeleteOrderRemotely = onDeleteOrderRemotely,
            onDeleteOrderShowSnackbar = onDeleteOrderShowSnackbar,
            onVisible = { visible = it },
            networkStatus = networkStatus,
          )
          Spacer(modifier = Modifier.height(contentHeight.large))
        }
      }
    }
  }
}

@Preview
@Composable
private fun OrderItemPrev() {
  val order =
    Order(
      recordId = 0,
      orderId = 0,
      customer = "Customer",
      date = 0L,
      title = "Title",
      model = "Model",
      size = "Size",
      color = "Color",
      category = "Category",
      quantity = 0,
      status = OrderStatus.Idle,
      creatorId = 0,
    )

  AppTheme {
    OrderItem(
      order = order,
      onAddNewOrderLocally = { _ -> },
      onDeleteOrderLocally = { _ -> },
      onDeleteOrderRemotely = { _, _ -> false },
      onDeleteOrderShowSnackbar = { SnackbarResult.Dismissed },
      networkStatus = NetworkStatus.Available,
    )
  }
}