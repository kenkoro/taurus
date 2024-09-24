package com.kenkoro.taurus.client.feature.orders.presentation.screen.order.components.bars

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kenkoro.taurus.client.R
import com.kenkoro.taurus.client.core.local.LocalContentHeight
import com.kenkoro.taurus.client.core.local.LocalContentWidth
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.util.OrderScreenNavigator
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.util.OrderScreenSnackbarsHolder
import com.kenkoro.taurus.client.feature.profile.domain.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun OrderTopBar(
  modifier: Modifier = Modifier,
  snackbarsHolder: OrderScreenSnackbarsHolder,
  navigator: OrderScreenNavigator,
  user: User?,
) {
  val userFirstName = user?.firstName ?: ""
  val contentWidth = LocalContentWidth.current
  val contentHeight = LocalContentHeight.current
  val scope = rememberCoroutineScope()

  Row(
    modifier =
      modifier
        .fillMaxWidth()
        .height(contentHeight.topBar)
        .background(MaterialTheme.colorScheme.background),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Box(modifier = Modifier.size(contentHeight.topBar)) {
      Box(
        modifier =
          Modifier
            .padding(8.dp)
            .fillMaxSize()
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.background)
            .border(
              width = 2.dp,
              shape = CircleShape,
              color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
            .clickable { navigator.toProfileScreen() },
        contentAlignment = Alignment.Center,
        content = {
          if (userFirstName.isNotEmpty()) {
            Text(
              text = userFirstName.first().toString(),
              modifier = Modifier.fillMaxWidth(),
              textAlign = TextAlign.Center,
            )
          }
        },
      )
    }
    Row(
      modifier =
        Modifier
          .fillMaxHeight()
          .fillMaxWidth(.8F)
          .clickable {
            scope.launch(Dispatchers.Main) { snackbarsHolder.notImplementedError() }
          },
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.Center,
    ) {
      Text(text = stringResource(id = R.string.all_orders))
      Spacer(modifier = Modifier.width(contentWidth.medium))
      Icon(
        imageVector = Icons.Default.KeyboardArrowDown,
        contentDescription = "order top bar composable: select a concrete customer",
      )
    }
    Box(
      modifier =
        Modifier
          .size(contentHeight.topBar)
          .clickable {
            scope.launch(Dispatchers.Main) { snackbarsHolder.notImplementedError() }
          },
    ) {
      Box(
        modifier =
          Modifier
            .padding(5.dp)
            .fillMaxSize(),
        contentAlignment = Alignment.Center,
      ) {
        Icon(
          imageVector = Icons.AutoMirrored.Default.Sort,
          contentDescription = "order top bar composable: sort orders",
        )
      }
    }
  }
}