package com.kenkoro.taurus.client.feature.sewing.presentation.screen.order.components

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.kenkoro.taurus.client.R
import com.kenkoro.taurus.client.core.connectivity.NetworkStatus
import com.kenkoro.taurus.client.core.local.LocalContentHeight
import com.kenkoro.taurus.client.core.local.LocalContentWidth
import com.kenkoro.taurus.client.core.local.LocalShape
import com.kenkoro.taurus.client.feature.sewing.data.source.mappers.toOrderDto
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.request.OrderRequestDto
import com.kenkoro.taurus.client.feature.sewing.data.util.OrderStatus
import com.kenkoro.taurus.client.feature.sewing.domain.model.Order
import com.kenkoro.taurus.client.feature.sewing.presentation.screen.login.components.FieldData
import com.kenkoro.taurus.client.feature.sewing.presentation.shared.components.showSnackbar
import com.kenkoro.taurus.client.feature.sewing.presentation.shared.handlers.remotelyCreateANewOrderWithLocallyScopedCredentials
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun OrderBottomBar(
  networkStatus: NetworkStatus,
  isLoginFailed: Boolean,
  scope: CoroutineScope,
  snackbarHostState: SnackbarHostState,
  onUpsertOrderLocally: suspend (Order) -> Unit,
  onUpsertOrderRemotely: suspend (OrderRequestDto, String) -> Unit,
) {
  val context = LocalContext.current
  val shape = LocalShape.current
  val contentWidth = LocalContentWidth.current
  val contentHeight = LocalContentHeight.current

  var expanded by rememberSaveable {
    mutableStateOf(false)
  }
  val orderBottomBarHeightAnimation by animateDpAsState(
    targetValue =
      if (expanded) {
        contentHeight.standard * 9
      } else {
        contentHeight.standard
      },
    animationSpec = tween(500),
    label = "OrderBottomBarHeightAnimation",
  )
  var isError by remember {
    mutableStateOf(false)
  }
  var wasRequestError by remember {
    mutableStateOf(false)
  }

  var orderId by remember {
    mutableStateOf("")
  }
  var customer by remember {
    mutableStateOf("")
  }
  var title by remember {
    mutableStateOf("")
  }
  var model by remember {
    mutableStateOf("")
  }
  var size by remember {
    mutableStateOf("")
  }
  var color by remember {
    mutableStateOf("")
  }
  var category by remember {
    mutableStateOf("")
  }
  var quantity by remember {
    mutableStateOf("")
  }

  LaunchedEffect(isLoginFailed, networkStatus) {
    if (networkStatus != NetworkStatus.Available || isLoginFailed) {
      expanded = false
    }
  }

  showSnackbar(
    snackbarHostState = snackbarHostState,
    key = wasRequestError,
    message = stringResource(id = R.string.request_error),
    extraCondition = wasRequestError,
  )

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
      enabled = networkStatus == NetworkStatus.Available && !isLoginFailed,
      modifier =
        Modifier
          .size(contentWidth.standard + 30.dp, contentHeight.halfStandard),
      shape = RoundedCornerShape(shape.small),
      onClick = {
        expanded = !expanded
        wasRequestError = false
      },
    ) {
      if (expanded) {
        Icon(
          imageVector = Icons.Default.KeyboardArrowUp,
          contentDescription = "CancelTheCreationOfANewOrder",
        )
      } else {
        Icon(imageVector = Icons.Default.Add, contentDescription = "AddANewOrder")
      }
    }
    Spacer(modifier = Modifier.height(contentHeight.medium))
    if (expanded) {
      val orderFields =
        listOf(
          FieldData(
            value = orderId,
            hint = stringResource(id = R.string.order_id_field),
            onValueChange = { orderId = it },
            keyboardOptions =
              KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Decimal,
              ),
          ),
          FieldData(
            value = customer,
            hint = stringResource(id = R.string.order_customer),
            onValueChange = { customer = it },
          ),
          FieldData(
            value = title,
            hint = stringResource(id = R.string.order_title),
            onValueChange = { title = it },
          ),
          FieldData(
            value = model,
            hint = stringResource(id = R.string.order_model),
            onValueChange = { model = it },
          ),
          FieldData(
            value = size,
            hint = stringResource(id = R.string.order_size),
            onValueChange = { size = it },
          ),
          FieldData(
            value = color,
            hint = stringResource(id = R.string.order_color),
            onValueChange = { color = it },
          ),
          FieldData(
            value = category,
            hint = stringResource(id = R.string.order_category),
            onValueChange = { category = it },
          ),
          FieldData(
            value = quantity,
            hint = stringResource(id = R.string.order_quantity),
            onValueChange = { quantity = it },
            keyboardOptions =
              KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Decimal,
              ),
          ),
        )

      LazyColumn {
        items(orderFields) { fieldData: FieldData ->
          OutlinedTextField(
            isError = isError,
            value = fieldData.value,
            onValueChange = {
              if (isError) {
                isError = false
              }
              fieldData.onValueChange(it)
            },
            shape = RoundedCornerShape(shape.large),
            label = {
              Text(text = fieldData.hint)
            },
            keyboardOptions = fieldData.keyboardOptions,
            keyboardActions = fieldData.keyboardActions,
            modifier = Modifier.width(contentWidth.standard + 30.dp),
            singleLine = true,
          )
        }
      }
      Spacer(modifier = Modifier.height(contentHeight.medium))
      Button(
        enabled = networkStatus == NetworkStatus.Available && !isLoginFailed,
        modifier =
          Modifier
            .size(contentWidth.standard + 30.dp, contentHeight.halfStandard),
        shape = RoundedCornerShape(shape.small),
        onClick = {
          if (!areOrderFieldsValid(
              listOf(
                orderId,
                customer,
                title,
                model,
                size,
                color,
                category,
                quantity,
              ),
            )
          ) {
            isError = true
          }

          if (!isError) {
            scope.launch {
              val order =
                Order(
                  orderId = orderId.toInt(),
                  customer = customer,
                  date = now(),
                  title = title,
                  model = model,
                  size = size,
                  color = color,
                  category = category,
                  quantity = quantity.toInt(),
                  status = OrderStatus.NotStarted,
                )
              onUpsertOrderLocally(order)
              expanded = !expanded
              val orderDto = order.toOrderDto()
              remotelyCreateANewOrderWithLocallyScopedCredentials(
                context,
                orderDto,
              ) { request, token ->
                try {
                  onUpsertOrderRemotely(request, token)
                } catch (e: Exception) {
                  wasRequestError = true
                }
              }

              /*
               * TODO: Refactor this and also get rid of the lazy column.
               * Instead, use a new composable.
               */
              orderId = ""
              customer = ""
              title = ""
              model = ""
              size = ""
              color = ""
              category = ""
              quantity = ""
            }
          }
        },
      ) {
        Text(text = stringResource(id = R.string.new_order_button))
      }
    }
  }
}

private fun areOrderFieldsValid(fields: List<String>): Boolean {
  fields.forEach { if (it.isBlank()) return false }
  return true
}

@SuppressLint("SimpleDateFormat")
private fun now(): String {
  val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm")
  val date = Date()
  return formatter.format(date)
}