package com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import com.kenkoro.taurus.client.core.local.LocalContentHeight
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.util.OrderStatesHolder
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.util.TextFieldValidationService
import com.kenkoro.taurus.client.feature.shared.components.TaurusIcon
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun OrderEditorTextFields(
  modifier: Modifier = Modifier,
  orderStatesHolder: OrderStatesHolder = OrderStatesHolder(),
  onNavUp: () -> Unit = {},
  saveChanges: suspend () -> Boolean = { false },
  validateChanges: () -> Boolean = { false },
) {
  val scope = rememberCoroutineScope()
  val focusManager = LocalFocusManager.current
  val contentHeight = LocalContentHeight.current

  val onSubmit = {
    focusManager.clearFocus()

    scope.launch(Dispatchers.IO) {
      if (validateChanges()) {
        val result = saveChanges()
        if (result) {
          withContext(Dispatchers.Main) { onNavUp() }
        }
      } else {
        TextFieldValidationService.checkAll(orderStatesHolder)
      }
    }
  }
  val goToNext = {
    focusManager.moveFocus(FocusDirection.Next)
    Unit
  }

  Column(
    modifier = modifier,
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Top,
  ) {
    Spacer(modifier = Modifier.height(contentHeight.large))
    val choices =
      remember {
        mutableStateListOf(
          "0",
          "1",
          "2",
          "3",
          "4",
          "5",
          "6",
          "7",
          "8",
          "9",
        )
      }
    TaurusDropDown(
      state = orderStatesHolder.quantityState,
      choices = choices,
      leadingIcon = {
        TaurusIcon(
          imageVector = Icons.Default.AccountBalance,
          contentDescription = "QuantityLeadingIcon",
          isError = orderStatesHolder.quantityState.showErrors(),
        )
      },
      supportingText = { Text(text = "Some supporting text") },
    )
    Spacer(modifier = Modifier.height(contentHeight.medium))
    OrderCustomer(
      customerState = orderStatesHolder.customerState,
      onImeAction = goToNext,
    )
    Spacer(modifier = Modifier.height(contentHeight.large))
    OrderTitle(
      titleState = orderStatesHolder.titleState,
      onImeAction = goToNext,
    )
    Spacer(modifier = Modifier.height(contentHeight.large))
    OrderModel(
      modelState = orderStatesHolder.modelState,
      onImeAction = goToNext,
    )
    Spacer(modifier = Modifier.height(contentHeight.large))
    OrderSize(
      sizeState = orderStatesHolder.sizeState,
      onImeAction = goToNext,
    )
    Spacer(modifier = Modifier.height(contentHeight.large))
    OrderColor(
      colorState = orderStatesHolder.colorState,
      onImeAction = goToNext,
    )
    Spacer(modifier = Modifier.height(contentHeight.large))
    OrderCategory(
      categoryState = orderStatesHolder.categoryState,
      onImeAction = goToNext,
    )
//    Spacer(modifier = Modifier.height(contentHeight.large))
//    OrderQuantity(
//      quantityState = orderStatesHolder.quantityState,
//      onImeAction = { onSubmit() },
//    )
  }
}