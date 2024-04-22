package com.kenkoro.taurus.client.feature.sewing.presentation.screen.order.components

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kenkoro.taurus.client.R
import com.kenkoro.taurus.client.core.connectivity.NetworkStatus
import com.kenkoro.taurus.client.core.local.LocalContentHeight
import com.kenkoro.taurus.client.core.local.LocalContentWidth
import com.kenkoro.taurus.client.core.local.LocalShape
import com.kenkoro.taurus.client.feature.sewing.data.util.OrderStatus
import com.kenkoro.taurus.client.feature.sewing.domain.model.Order
import com.kenkoro.taurus.client.feature.sewing.domain.model.User
import com.kenkoro.taurus.client.feature.sewing.presentation.util.DecryptedCredentialService
import com.kenkoro.taurus.client.ui.theme.AppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun OrderItem(
  order: Order,
  user: User?,
  scope: CoroutineScope,
  isLoginFailed: Boolean,
  networkStatus: NetworkStatus,
  snackbarHostState: SnackbarHostState,
  onDeleteOrderRemotely: suspend (Int, String, String) -> Unit,
  onDeleteOrderLocally: suspend (Order) -> Unit,
  onUpsertOrderLocally: suspend (Order) -> Unit,
) {
  val context = LocalContext.current
  val shape = LocalShape.current
  val contentWidth = LocalContentWidth.current
  val contentHeight = LocalContentHeight.current

  val orderWasDeletedMessage = stringResource(id = R.string.order_was_deleted)
  val okActionLabel = stringResource(id = R.string.ok)

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
  val credentialService = DecryptedCredentialService(context)

  AnimatedVisibility(visible = visible) {
    Column {
      Spacer(modifier = Modifier.height(contentHeight.medium))
      Column(
        modifier =
          Modifier
            .width(contentWidth.standard + 30.dp)
            .height(heightAnimated)
            .clip(RoundedCornerShape(shape.medium))
            .background(MaterialTheme.colorScheme.primaryContainer)
            .clickable { clicked = !clicked },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(contentHeight.large),
      ) {
        Spacer(modifier = Modifier.height(contentHeight.small))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceEvenly,
          verticalAlignment = Alignment.CenterVertically,
        ) {
          Column(
            modifier = Modifier.width(contentWidth.halfStandard),
            horizontalAlignment = Alignment.Start,
          ) {
            Text(
              text = order.date,
              color = MaterialTheme.colorScheme.onPrimaryContainer,
              style = MaterialTheme.typography.bodySmall,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.height(contentHeight.small))
            Text(
              text = order.title,
              color = MaterialTheme.colorScheme.onBackground,
              style = MaterialTheme.typography.bodyMedium,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis,
            )
          }

          when (order.status) {
            OrderStatus.NotStarted -> {
              Text(
                text = stringResource(id = R.string.order_status_not_started),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = MaterialTheme.typography.labelMedium,
              )
            }

            OrderStatus.Cut -> {
              Text(
                text = stringResource(id = R.string.order_status_cutted),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = MaterialTheme.typography.labelMedium,
              )
            }

            OrderStatus.Checked -> {
              Text(
                text = stringResource(id = R.string.order_status_checked),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = MaterialTheme.typography.labelMedium,
              )
            }
          }
        }

        if (clicked) {
          val orderInfo =
            listOf(
              Pair(stringResource(id = R.string.order_id), order.orderId.toString()),
              Pair(stringResource(id = R.string.order_size), order.size),
              Pair(stringResource(id = R.string.order_model), order.model),
              Pair(stringResource(id = R.string.order_category), order.category),
              Pair(stringResource(id = R.string.order_color), order.color),
              Pair(stringResource(id = R.string.order_customer), order.customer),
              Pair(stringResource(id = R.string.order_quantity), order.quantity.toString()),
            )
          LazyColumn(modifier = Modifier.width(contentWidth.standard)) {
            item {
              Spacer(modifier = Modifier.height(contentHeight.large))
            }
            items(orderInfo) { pair ->
              Row {
                Spacer(modifier = Modifier.width(contentWidth.large))
                Text(
                  text = "${pair.first}: ${pair.second}",
                  color = MaterialTheme.colorScheme.onBackground,
                  style = MaterialTheme.typography.bodyMedium,
                  maxLines = 1,
                  overflow = TextOverflow.Ellipsis,
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(contentHeight.large))
          Button(
            enabled = networkStatus == NetworkStatus.Available && !isLoginFailed,
            modifier =
              Modifier
                .size(contentWidth.standard, contentHeight.halfStandard),
            shape = RoundedCornerShape(shape.small),
            onClick = {
              scope.launch(Dispatchers.IO) {
                launch {
                  visible = false

                  delay(500L)
                  onDeleteOrderLocally(order)
                  try {
                    onDeleteOrderRemotely(
                      order.orderId,
                      credentialService.storedToken().value,
                      user?.subject ?: "",
                    )
                  } catch (e: Exception) {
                    Log.d("kenkoro", e.message!!)
                  }
                }

                launch {
                  snackbarHostState.showSnackbar(
                    message = orderWasDeletedMessage,
                    actionLabel = okActionLabel,
                    duration = SnackbarDuration.Short,
                  )
                }
              }
            },
          ) {
            Row {
              Text(text = stringResource(id = R.string.delete_button))
            }
          }
        }
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
private fun OrderItemPrev() {
  val snackbarHostState = remember { SnackbarHostState() }
  val scope = rememberCoroutineScope()

  AppTheme {
    OrderItem(
      scope = scope,
      order =
        Order(
          orderId = 0,
          customer = "Customer",
          date = "29.03.2024",
          title = "Titleasdflnaeopifnasdfonaspdfnasdfnsdjfjasnsfdn",
          model = "Model",
          size = "Size",
          color = "Color",
          category = "Category",
          quantity = 0,
          status = OrderStatus.NotStarted,
        ),
      user = null,
      isLoginFailed = false,
      onDeleteOrderRemotely = { _, _, _ -> },
      onDeleteOrderLocally = { _ -> },
      onUpsertOrderLocally = { _ -> },
      networkStatus = NetworkStatus.Available,
      snackbarHostState = snackbarHostState,
    )
  }
}