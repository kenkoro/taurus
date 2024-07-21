package com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.composables

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Colorize
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Title
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import com.kenkoro.taurus.client.R
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
    Spacer(modifier = Modifier.height(contentHeight.medium))
    TaurusDropDown(
      state = orderStatesHolder.customerState,
      choices = customers(),
      leadingIcon = {
        TaurusIcon(
          imageVector = Icons.Default.Person,
          contentDescription = "CustomerLeadingIcon",
          isError = orderStatesHolder.customerState.showErrors(),
        )
      },
      supportingText = {
        Text(
          text = stringResource(id = R.string.order_customer_supporting_text),
        )
      },
      onImeAction = goToNext,
    )
    Spacer(modifier = Modifier.height(contentHeight.large))
    TaurusDropDown(
      state = orderStatesHolder.titleState,
      choices = titles(),
      leadingIcon = {
        TaurusIcon(
          imageVector = Icons.Default.Title,
          contentDescription = "TitleLeadingIcon",
          isError = orderStatesHolder.titleState.showErrors(),
        )
      },
      supportingText = { Text(text = stringResource(id = R.string.order_title_supporting_text)) },
      onImeAction = goToNext,
    )
    Spacer(modifier = Modifier.height(contentHeight.large))
    TaurusDropDown(
      state = orderStatesHolder.modelState,
      choices = emptyList(),
      leadingIcon = {
        TaurusIcon(
          imageVector = Icons.Default.Group,
          contentDescription = "ModelLeadingIcon",
          isError = orderStatesHolder.modelState.showErrors(),
        )
      },
      supportingText = { Text(text = stringResource(id = R.string.order_model_supporting_text)) },
      onImeAction = goToNext,
    )
    Spacer(modifier = Modifier.height(contentHeight.large))
    TaurusDropDown(
      state = orderStatesHolder.sizeState,
      choices = sizes(),
      leadingIcon = {
        TaurusIcon(
          imageVector = Icons.Default.FormatSize,
          contentDescription = "SizeLeadingIcon",
          isError = orderStatesHolder.sizeState.showErrors(),
        )
      },
      supportingText = { Text(text = stringResource(id = R.string.order_size_supporting_text)) },
      onImeAction = goToNext,
    )
    Spacer(modifier = Modifier.height(contentHeight.large))
    TaurusDropDown(
      state = orderStatesHolder.colorState,
      choices = emptyList(),
      leadingIcon = {
        TaurusIcon(
          imageVector = Icons.Default.Colorize,
          contentDescription = "ColorLeadingIcon",
          isError = orderStatesHolder.colorState.showErrors(),
        )
      },
      supportingText = { Text(text = stringResource(id = R.string.order_color_supporting_text)) },
      onImeAction = goToNext,
    )
    Spacer(modifier = Modifier.height(contentHeight.large))
    TaurusDropDown(
      state = orderStatesHolder.colorState,
      choices = emptyList(),
      leadingIcon = {
        TaurusIcon(
          imageVector = Icons.Default.Category,
          contentDescription = "CategoryLeadingIcon",
          isError = orderStatesHolder.categoryState.showErrors(),
        )
      },
      supportingText = {
        Text(
          text = stringResource(id = R.string.order_category_supporting_text),
        )
      },
      onImeAction = goToNext,
    )
    Spacer(modifier = Modifier.height(contentHeight.large))
    OrderQuantity(
      quantityState = orderStatesHolder.quantityState,
      onImeAction = { onSubmit() },
    )
  }
}

@SuppressLint("ComposableNaming")
@Composable
fun customers(modifier: Modifier = Modifier): List<String> {
  val customers =
    remember {
      mutableStateListOf(
        "Suborbia",
      )
    }

  return customers
}

@SuppressLint("ComposableNaming")
@Composable
fun titles(modifier: Modifier = Modifier): List<String> {
  val titles =
    remember {
      mutableStateListOf(
        "Футболка Terminals",
        "Джоггеры Terminals",
        "Палаццо Terminals",
        "Свитшот Terminals",
        "Футболка Cities",
      )
    }

  return titles
}

@SuppressLint("ComposableNaming")
@Composable
fun sizes(modifier: Modifier = Modifier): List<String> {
  val sizes =
    remember {
      mutableStateListOf(
        "S",
        "M",
        "L",
        "X/S",
        "S/M",
        "M/L",
        "O/S",
      )
    }

  return sizes
}