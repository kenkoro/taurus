package com.kenkoro.taurus.client.feature.orders.presentation.screen.order.components.item.order

import android.os.Build
import android.os.VibrationEffect
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalView
import com.kenkoro.taurus.client.core.local.LocalContentHeight
import com.kenkoro.taurus.client.core.local.LocalContentWidth
import com.kenkoro.taurus.client.core.local.LocalShape
import com.kenkoro.taurus.client.feature.orders.domain.Order
import com.kenkoro.taurus.client.feature.orders.domain.OrderStatus
import com.kenkoro.taurus.client.feature.orders.domain.OrderStatus.Cut
import com.kenkoro.taurus.client.feature.orders.domain.OrderStatus.Idle
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.states.OrderStatesHolder
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.components.allowedToSeeOrders
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.util.OrderScreenLocalHandler
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.util.OrderScreenNavigator
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.util.OrderScreenRemoteHandler
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.util.OrderScreenSnackbarsHolder
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.util.OrderScreenUtils
import com.kenkoro.taurus.client.feature.profile.domain.UserProfile
import com.kenkoro.taurus.client.feature.profile.domain.UserProfile.Admin
import com.kenkoro.taurus.client.feature.profile.domain.UserProfile.Ceo
import com.kenkoro.taurus.client.feature.profile.domain.UserProfile.Customer
import com.kenkoro.taurus.client.feature.profile.domain.UserProfile.Cutter
import com.kenkoro.taurus.client.feature.profile.domain.UserProfile.Inspector
import com.kenkoro.taurus.client.feature.profile.domain.UserProfile.Manager

@Composable
fun OrderItem(
  modifier: Modifier = Modifier,
  order: Order,
  localHandler: OrderScreenLocalHandler,
  remoteHandler: OrderScreenRemoteHandler,
  navigator: OrderScreenNavigator,
  utils: OrderScreenUtils,
  statesHolder: OrderStatesHolder,
  snackbarsHolder: OrderScreenSnackbarsHolder,
  onRefresh: () -> Unit = {},
) {
  val user = utils.user

  val shape = LocalShape.current
  val contentWidth = LocalContentWidth.current
  val contentHeight = LocalContentHeight.current
  val view = LocalView.current
  val vibrationEffect =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
      VibrationEffect.EFFECT_CLICK
    } else {
      null
    }

  val onSaveStates = {
    statesHolder.customerState.text = order.customer
    statesHolder.titleState.text = order.title
    statesHolder.modelState.text = order.model
    statesHolder.sizeState.text = order.size
    statesHolder.colorState.text = order.color
    statesHolder.categoryState.text = order.category
    statesHolder.quantityState.text = order.quantity.toString()

    utils.saveOrderId(order.orderId)
    utils.saveOrderStatus(order.status)
    utils.saveDate(order.date)
  }
  val onEditOrder = {
    onSaveStates()
    navigator.toOrderEditorScreen(true)
  }
  val onDropOrderSelection = { utils.selectOrder(null) }

  var visible by rememberSaveable {
    mutableStateOf(true)
  }
  val selected = { utils.selectedOrderRecordId == order.recordId }
  val animatedHeight by animateDpAsState(
    targetValue =
      if (selected()) {
        if (
          allowedToUpdateOrderStatus(user?.profile) &&
          orderStatusCorrespondsToUserProfile(user?.profile, order.status)
        ) {
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
    Column(modifier = modifier) {
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
                onDropOrderSelection()
              } else {
                utils.selectOrder(order.recordId)
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
            selected = selected(),
            isCutter = user != null && user.profile == Cutter,
            onGetActualCutOrdersQuantity = { remoteHandler.getActualCutOrdersQuantity(it) },
          )
        }

        if (allowedToUpdateOrderStatus(user?.profile) && selected()) {
          Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom,
          ) {
            Row(
              modifier = Modifier.width(contentWidth.standard),
              horizontalArrangement = Arrangement.Center,
              verticalAlignment = Alignment.CenterVertically,
            ) {
              OrderItemBottomActionButton(
                modifier = Modifier.weight(1F),
                order = order,
                localHandler = localHandler,
                remoteHandler = remoteHandler,
                utils = utils,
                snackbarsHolder = snackbarsHolder,
                onHide = { visible = false },
                onRefresh = onRefresh,
              )

              if (allowedToEditOrder(user?.profile)) {
                Spacer(modifier = Modifier.width(contentWidth.small))
                IconButton(
                  onClick = {
                    vibrationEffect?.let {
                      view.performHapticFeedback(it)
                    }
                    onEditOrder()
                  },
                ) {
                  Icon(imageVector = Icons.Default.Edit, contentDescription = "EditOrder")
                }
              }
            }
            Spacer(modifier = Modifier.height(contentHeight.large))
          }
        }
      }
    }
  }
}

private fun allowedToUpdateOrderStatus(profile: UserProfile?): Boolean {
  return allowedToSeeOrders(profile) &&
    profile != Admin &&
    profile != Ceo &&
    profile != Manager
}

private fun allowedToEditOrder(profile: UserProfile?): Boolean {
  return profile != null && profile == Customer
}

private fun orderStatusCorrespondsToUserProfile(
  userProfile: UserProfile?,
  orderStatus: OrderStatus,
): Boolean {
  return when (userProfile) {
    Cutter -> orderStatus == Idle
    Inspector -> orderStatus == Cut
    null -> false
    else -> true
  }
}