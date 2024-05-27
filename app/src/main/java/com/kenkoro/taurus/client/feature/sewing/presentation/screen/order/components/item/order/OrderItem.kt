package com.kenkoro.taurus.client.feature.sewing.presentation.screen.order.components.item.order

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
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
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.NewOrderDto
import com.kenkoro.taurus.client.feature.sewing.domain.model.NewOrder
import com.kenkoro.taurus.client.feature.sewing.domain.model.Order
import com.kenkoro.taurus.client.feature.sewing.domain.model.enums.OrderStatus
import com.kenkoro.taurus.client.feature.sewing.domain.model.enums.UserProfile
import com.kenkoro.taurus.client.feature.sewing.domain.model.enums.UserProfile.Admin
import com.kenkoro.taurus.client.feature.sewing.domain.model.enums.UserProfile.Ceo
import com.kenkoro.taurus.client.feature.sewing.domain.model.enums.UserProfile.Manager
import com.kenkoro.taurus.client.feature.sewing.presentation.screen.order.components.allowedToSeeOrders
import com.kenkoro.taurus.client.ui.theme.AppTheme

@Composable
fun OrderItem(
  modifier: Modifier = Modifier,
  order: Order,
  profile: UserProfile,
  networkStatus: NetworkStatus,
  selectedOrderRecordId: Int? = null,
  onSelectOrder: (Int?) -> Unit = {},
  onAddNewOrderLocally: suspend (NewOrder) -> Unit,
  onDeleteOrderLocally: suspend (Order) -> Unit,
  onEditOrderLocally: suspend (NewOrder) -> Unit,
  onDeleteOrderRemotely: suspend (orderId: Int, deleterSubject: String) -> Boolean,
  onEditOrderRemotely: suspend (NewOrderDto, Int, String, String) -> Boolean,
  onDeleteOrderShowSnackbar: suspend () -> SnackbarResult,
) {
  val shape = LocalShape.current
  val contentWidth = LocalContentWidth.current
  val contentHeight = LocalContentHeight.current

  var visible by rememberSaveable {
    mutableStateOf(true)
  }
  val selected = { selectedOrderRecordId == order.recordId }
  val animatedHeight by animateDpAsState(
    targetValue =
      if (selected()) {
        if (allowedToUpdateOrderStatus(profile)) {
          contentHeight.orderItemExpanded
        } else {
          contentHeight.orderItemExpandedWithoutActionButton
        }
      } else {
        contentHeight.orderItemNotExpanded
      },
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
            .height(animatedHeight)
            .clip(RoundedCornerShape(shape.medium))
            .background(MaterialTheme.colorScheme.primaryContainer)
            .clickable {
              if (selected()) {
                onSelectOrder(null)
              } else {
                onSelectOrder(order.recordId)
              }
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
      ) {
        Column(
          modifier =
            Modifier
              .fillMaxWidth()
              .wrapContentHeight(),
          verticalArrangement = Arrangement.Top,
          horizontalAlignment = Alignment.CenterHorizontally,
        ) {
          Spacer(modifier = Modifier.height(contentHeight.large))
          OrderItemContent(
            order = order,
            clicked = selected(),
          )
          Spacer(modifier = Modifier.height(contentHeight.large))
        }

        if (allowedToUpdateOrderStatus(profile) && selected()) {
          Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom,
          ) {
            OrderItemButtonHandler(
              order = order,
              profile = profile,
              onAddNewOrderLocally = onAddNewOrderLocally,
              onDeleteOrderLocally = onDeleteOrderLocally,
              onEditOrderLocally = onEditOrderLocally,
              onDeleteOrderRemotely = onDeleteOrderRemotely,
              onEditOrderRemotely = onEditOrderRemotely,
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
}

private fun allowedToUpdateOrderStatus(profile: UserProfile): Boolean {
  return allowedToSeeOrders(profile) &&
    profile != Admin &&
    profile != Ceo &&
    profile != Manager
}

@Preview(showBackground = true)
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
      status = OrderStatus.Checked,
      creatorId = 0,
    )

  AppTheme {
    OrderItem(
      profile = UserProfile.Customer,
      order = order,
      onAddNewOrderLocally = { _ -> },
      onDeleteOrderLocally = { _ -> },
      onEditOrderLocally = { _ -> },
      onDeleteOrderRemotely = { _, _ -> false },
      onEditOrderRemotely = { _, _, _, _ -> false },
      onDeleteOrderShowSnackbar = { SnackbarResult.Dismissed },
      networkStatus = NetworkStatus.Available,
    )
  }
}