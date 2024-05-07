package com.kenkoro.taurus.client.feature.sewing.presentation.screen.order.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.kenkoro.taurus.client.R
import com.kenkoro.taurus.client.core.connectivity.NetworkStatus
import com.kenkoro.taurus.client.core.crypto.DecryptedCredentialService
import com.kenkoro.taurus.client.core.local.LocalContentHeight
import com.kenkoro.taurus.client.core.local.LocalStrokeWidth
import com.kenkoro.taurus.client.feature.sewing.domain.model.NewOrder
import com.kenkoro.taurus.client.feature.sewing.domain.model.Order
import kotlinx.coroutines.launch
import java.util.UUID

@Composable
fun LazyOrdersContent(
  orders: LazyPagingItems<Order>,
  onAddNewOrderLocally: suspend (NewOrder) -> Unit,
  onDeleteOrderLocally: suspend (Order) -> Unit,
  onDeleteOrderRemotely: suspend (orderId: Int, deleterSubject: String) -> Boolean,
  snackbarHostState: SnackbarHostState,
  errorSnackbarHostState: SnackbarHostState,
  decryptedCredentialService: DecryptedCredentialService,
  networkStatus: NetworkStatus,
  modifier: Modifier = Modifier,
) {
  val contentHeight = LocalContentHeight.current
  val strokeWidth = LocalStrokeWidth.current

  val errorRequestMessage = stringResource(id = R.string.request_error)
  val orderWasDeletedMessage = stringResource(id = R.string.order_was_deleted)
  val okActionLabel = stringResource(id = R.string.ok)

  if (orders.loadState.append is LoadState.Error) {
    LaunchedEffect(Unit) {
      launch {
        errorSnackbarHostState.showSnackbar(
          message = errorRequestMessage,
          actionLabel = okActionLabel,
        )
      }
    }
  }

  LazyColumn(
    modifier = modifier.fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    item {
      Spacer(modifier = Modifier.height(contentHeight.large))
    }
    items(
      count = orders.itemCount,
      key = { index -> orders[index]?.orderId ?: UUID.randomUUID().toString() },
    ) { index ->
      val order = orders[index]
      if (order != null) {
        OrderItem(
          order = order,
          onAddNewOrderLocally = onAddNewOrderLocally,
          onDeleteOrderLocally = onDeleteOrderLocally,
          onDeleteOrderRemotely = onDeleteOrderRemotely,
          onShowSnackbar = {
            snackbarHostState.showSnackbar(
              message = orderWasDeletedMessage,
              actionLabel = okActionLabel,
              duration = SnackbarDuration.Short,
            )
          },
          networkStatus = networkStatus,
          decryptedCredentialService = decryptedCredentialService,
        )
      }
    }
    item {
      if (orders.loadState.append is LoadState.Loading) {
        CircularProgressIndicator(strokeWidth = strokeWidth.standard)
      }
    }
    item {
      Spacer(modifier = Modifier.height(contentHeight.large))
    }
  }
}