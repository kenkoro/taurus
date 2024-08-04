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
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import com.kenkoro.taurus.client.core.connectivity.NetworkStatus
import com.kenkoro.taurus.client.core.local.LocalContentHeight
import com.kenkoro.taurus.client.core.local.LocalContentWidth
import com.kenkoro.taurus.client.core.local.LocalShape
import com.kenkoro.taurus.client.feature.login.data.mappers.toUserDto
import com.kenkoro.taurus.client.feature.orders.data.mappers.toCutOrderDto
import com.kenkoro.taurus.client.feature.orders.data.mappers.toOrderDto
import com.kenkoro.taurus.client.feature.orders.data.remote.dto.ActualCutOrdersQuantityDto
import com.kenkoro.taurus.client.feature.orders.domain.CutOrder
import com.kenkoro.taurus.client.feature.orders.domain.Order
import com.kenkoro.taurus.client.feature.orders.domain.OrderStatus
import com.kenkoro.taurus.client.feature.orders.domain.OrderStatus.Cut
import com.kenkoro.taurus.client.feature.orders.domain.OrderStatus.Idle
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.util.OrderStatesHolder
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.components.allowedToSeeOrders
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.handlers.LocalHandler
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.handlers.RemoteHandler
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.util.SnackbarsHolder
import com.kenkoro.taurus.client.feature.profile.domain.User
import com.kenkoro.taurus.client.feature.profile.domain.UserProfile
import com.kenkoro.taurus.client.feature.profile.domain.UserProfile.Admin
import com.kenkoro.taurus.client.feature.profile.domain.UserProfile.Ceo
import com.kenkoro.taurus.client.feature.profile.domain.UserProfile.Customer
import com.kenkoro.taurus.client.feature.profile.domain.UserProfile.Cutter
import com.kenkoro.taurus.client.feature.profile.domain.UserProfile.Inspector
import com.kenkoro.taurus.client.feature.profile.domain.UserProfile.Manager
import com.kenkoro.taurus.client.feature.shared.data.remote.dto.TokenDto
import com.kenkoro.taurus.client.ui.theme.AppTheme

@Composable
fun OrderItem(
  modifier: Modifier = Modifier,
  user: User,
  order: Order,
  networkStatus: NetworkStatus,
  selectedOrderRecordId: Int? = null,
  orderStatesHolder: OrderStatesHolder = OrderStatesHolder(),
  onSelectOrder: (Int?) -> Unit = {},
  localHandler: LocalHandler = LocalHandler(),
  remoteHandler: RemoteHandler,
  snackbarsHolder: SnackbarsHolder,
  onDecryptToken: () -> String,
  onRefresh: () -> Unit = {},
  onOrderStatus: (OrderStatus) -> Unit = {},
  onOrderId: (Int) -> Unit = {},
  onNavigateToOrderEditorScreen: (editOrder: Boolean) -> Unit = {},
) {
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
  val saveStates = {
    orderStatesHolder.customerState.text = order.customer
    orderStatesHolder.titleState.text = order.title
    orderStatesHolder.modelState.text = order.model
    orderStatesHolder.sizeState.text = order.size
    orderStatesHolder.colorState.text = order.color
    orderStatesHolder.categoryState.text = order.category
    orderStatesHolder.quantityState.text = order.quantity.toString()

    onOrderId(order.orderId)
    onOrderStatus(order.status)
  }
  val onEditOrder = {
    saveStates()
    onNavigateToOrderEditorScreen(true)
  }

  var visible by rememberSaveable {
    mutableStateOf(true)
  }
  val selected = { selectedOrderRecordId == order.recordId }
  val animatedHeight by animateDpAsState(
    targetValue =
      if (selected()) {
        if (
          allowedToUpdateOrderStatus(user.profile) &&
          orderStatusCorrespondsToUserProfile(user.profile, order.status)
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
            selected = selected(),
            isCutter = user.profile == Cutter,
            onGetActualCutOrdersQuantity = {
              remoteHandler.getActualCutOrdersQuantity(it)
            },
          )
        }

        if (allowedToUpdateOrderStatus(user.profile) && selected()) {
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
                networkStatus = networkStatus,
                userId = user.userId,
                userProfile = user.profile,
                userSubject = user.subject,
                localHandler = localHandler,
                remoteHandler = remoteHandler,
                snackbarsHolder = snackbarsHolder,
                onHide = { visible = false },
                onDecryptToken = onDecryptToken,
                onRefresh = onRefresh,
              )

              if (allowedToEditOrder(user.profile)) {
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

private fun allowedToUpdateOrderStatus(profile: UserProfile): Boolean {
  return allowedToSeeOrders(profile) &&
    profile != Admin &&
    profile != Ceo &&
    profile != Manager
}

private fun allowedToEditOrder(profile: UserProfile): Boolean {
  return profile == Customer
}

private fun orderStatusCorrespondsToUserProfile(
  userProfile: UserProfile,
  orderStatus: OrderStatus,
): Boolean {
  return when (userProfile) {
    Cutter -> orderStatus == Idle
    Inspector -> orderStatus == Cut
    else -> true
  }
}

@Preview(showBackground = true)
@Composable
private fun OrderItemPrev() {
  val cutOrder =
    CutOrder(
      cutOrderId = 0,
      orderId = 419,
      date = 0L,
      quantity = 3,
      cutterId = 0,
      comment = "",
    )
  val order =
    Order(
      recordId = 0,
      orderId = 410,
      customer = "Suborbia",
      date = 1717427278111L,
      title = "Some large title, hello world!",
      model = "Grand",
      size = "OS",
      color = "Black",
      category = "Жилеты",
      quantity = 4,
      status = Idle,
      creatorId = 0,
    )
  val user =
    User(
      userId = 0,
      subject = "Subject",
      password = "Password",
      image = "Image",
      firstName = "FirstName",
      lastName = "LastName",
      email = "Email",
      profile = Cutter,
      salt = "Salt",
    )
  val remoteHandler =
    RemoteHandler(
      login = { _, _ -> Result.success(TokenDto("")) },
      getUser = { _, _ -> Result.success(user.toUserDto()) },
      addNewOrder = { _ -> Result.success(order.toOrderDto()) },
      addNewCutOrder = { _ -> Result.success(cutOrder.toCutOrderDto()) },
      getActualCutOrdersQuantity = { _ ->
        Result.success(ActualCutOrdersQuantityDto(cutOrder.quantity))
      },
    )
  val snackbarHostState = remember { SnackbarHostState() }

  AppTheme {
    OrderItem(
      user = user,
      order = order,
      selectedOrderRecordId = 0,
      remoteHandler = remoteHandler,
      snackbarsHolder =
        SnackbarsHolder(
          snackbarHostState = snackbarHostState,
          errorSnackbarHostState = snackbarHostState,
          internetErrorSnackbarHostState = snackbarHostState,
        ),
      networkStatus = NetworkStatus.Available,
      onDecryptToken = { "" },
    )
  }
}