package com.kenkoro.taurus.client.feature.orders.presentation.screen.order.components.bars

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.kenkoro.taurus.client.R
import com.kenkoro.taurus.client.core.local.LocalContentHeight
import com.kenkoro.taurus.client.core.local.LocalContentWidth
import com.kenkoro.taurus.client.core.local.LocalOffset
import com.kenkoro.taurus.client.core.local.LocalShape
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.util.OrderScreenNavigator
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.util.OrderScreenShared
import com.kenkoro.taurus.client.feature.profile.domain.User

@Composable
fun OrderBottomBar(
  modifier: Modifier = Modifier,
  user: User?,
  navigator: OrderScreenNavigator,
  shared: OrderScreenShared,
  isScrolling: Boolean,
) {
  val offset = LocalOffset.current
  val shape = LocalShape.current
  val contentWidth = LocalContentWidth.current
  val contentHeight = LocalContentHeight.current

  val animatedYOffset by animateDpAsState(
    targetValue =
      if (isScrolling) {
        offset.bottomBar
      } else {
        offset.none
      },
    label = "order bottom bar composable: animated y offset",
  )
  val animatedBottomBarHeight by animateDpAsState(
    targetValue =
      if (isScrolling) {
        contentHeight.none
      } else {
        contentHeight.bottomBar
      },
    label = "order bottom bar composable: animated bottom bar height",
  )
  val animatedBottomBarButtonHeight by animateDpAsState(
    targetValue =
      if (isScrolling) {
        contentHeight.none
      } else {
        contentHeight.halfStandard
      },
    label = "order bottom bar composable: animated bottom bar button height",
  )

  Column(
    modifier =
      modifier
        .fillMaxWidth()
        .height(animatedBottomBarHeight)
        .offset(y = animatedYOffset),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center,
  ) {
    Button(
      modifier =
        Modifier
          .width(contentWidth.orderItem)
          .height(animatedBottomBarButtonHeight),
      onClick = {
        shared.resetAllOrderDetails()
        navigator.toOrderEditorScreen(false, user?.subject ?: "")
      },
      shape = RoundedCornerShape(shape.medium),
    ) {
      Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
      ) {
        Icon(
          imageVector = Icons.Default.AddCircle,
          contentDescription = "order bottom bar composable: add a new order",
        )
        Spacer(modifier = Modifier.width(contentWidth.medium))
        Text(text = stringResource(id = R.string.new_order_button))
      }
    }
  }
}