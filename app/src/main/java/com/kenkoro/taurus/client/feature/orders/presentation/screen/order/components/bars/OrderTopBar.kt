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
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kenkoro.taurus.client.R
import com.kenkoro.taurus.client.core.connectivity.NetworkStatus
import com.kenkoro.taurus.client.core.local.LocalContentHeight
import com.kenkoro.taurus.client.core.local.LocalContentWidth
import com.kenkoro.taurus.client.ui.theme.AppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun OrderTopBar(
  modifier: Modifier = Modifier,
  networkStatus: NetworkStatus,
  isScrollingInProgress: Boolean = false,
  userName: String? = null,
  onFilterOrdersShowSnackbar: suspend () -> SnackbarResult,
  onSortOrdersShowSnackbar: suspend () -> SnackbarResult,
  onNavigateToProfileScreen: () -> Unit,
) {
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
            .clickable { onNavigateToProfileScreen() },
        contentAlignment = Alignment.Center,
        content = {
          if (!userName.isNullOrEmpty()) {
            Text(
              text = userName.first().toString(),
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
            scope.launch(Dispatchers.Main) {
              onFilterOrdersShowSnackbar()
            }
          },
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.Center,
    ) {
      Text(text = stringResource(id = R.string.all_orders))
      Spacer(modifier = Modifier.width(contentWidth.medium))
      Icon(
        imageVector = Icons.Default.KeyboardArrowDown,
        contentDescription = "SelectACustomerForFilteringOrders",
      )
    }
    Box(
      modifier =
        Modifier
          .size(contentHeight.topBar)
          .clickable {
            scope.launch(Dispatchers.Main) {
              onSortOrdersShowSnackbar()
            }
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
          contentDescription = "SortOrders",
        )
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
private fun OrderTopBarPrev() {
  AppTheme {
    OrderTopBar(
      onSortOrdersShowSnackbar = { SnackbarResult.ActionPerformed },
      onFilterOrdersShowSnackbar = { SnackbarResult.ActionPerformed },
      onNavigateToProfileScreen = {},
      networkStatus = NetworkStatus.Available,
      userName = "Peter",
    )
  }
}