package com.kenkoro.taurus.client.feature.sewing.presentation.screen.order.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kenkoro.taurus.client.core.connectivity.Status
import com.kenkoro.taurus.client.core.local.LocalContentHeight
import com.kenkoro.taurus.client.core.local.LocalContentWidth
import com.kenkoro.taurus.client.core.local.LocalShape

@Composable
fun OrderBottomBar(
  networkStatus: Status,
  isLoginFailed: Boolean,
) {
  val shape = LocalShape.current
  val contentWidth = LocalContentWidth.current
  val contentHeight = LocalContentHeight.current

  var expanded by rememberSaveable {
    mutableStateOf(false)
  }
  val orderBottomBarHeightAnimation by animateDpAsState(
    targetValue = if (expanded) {
      contentHeight.standard * 7
    } else {
      contentHeight.standard
    },
    animationSpec = tween(500),
    label = "OrderBottomBarHeightAnimation"
  )

  LaunchedEffect(isLoginFailed, networkStatus) {
    if (networkStatus != Status.Available || isLoginFailed) {
      expanded = false
    }
  }

  Column(
    modifier =
    Modifier
      .fillMaxWidth()
      .height(orderBottomBarHeightAnimation)
      .background(MaterialTheme.colorScheme.background),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(contentHeight.small),
  ) {
    Spacer(modifier = Modifier.height(contentHeight.medium))
    Button(
      enabled = networkStatus == Status.Available && !isLoginFailed,
      modifier =
      Modifier
        .size(contentWidth.standard + 30.dp, contentHeight.halfStandard),
      shape = RoundedCornerShape(shape.small),
      onClick = { expanded = !expanded },
    ) {
      if (expanded) {
        Icon(
          imageVector = Icons.Default.KeyboardArrowUp,
          contentDescription = "CancelTheCreationOfANewOrder"
        )
      } else {
        Icon(imageVector = Icons.Default.Add, contentDescription = "AddANewOrder")
      }
    }
    Spacer(modifier = Modifier.height(contentHeight.medium))
    if (expanded) {
      LazyColumn {
        items(5) {
          OutlinedTextField(value = "", onValueChange = {}, shape = RoundedCornerShape(shape.large))
          Spacer(modifier = Modifier.height(contentHeight.large))
        }
      }
    }
  }
}