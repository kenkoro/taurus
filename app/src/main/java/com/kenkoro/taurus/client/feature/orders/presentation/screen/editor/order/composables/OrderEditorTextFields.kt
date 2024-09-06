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
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.composables.bars.util.OrderEditorScreenExtras
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.util.OrderEditorScreenNavigator
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.util.OrderStatesHolder
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.util.TextFieldValidationService
import com.kenkoro.taurus.client.feature.shared.components.TaurusIcon
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun OrderEditorTextFields(
  modifier: Modifier = Modifier,
  states: OrderStatesHolder = OrderStatesHolder(),
  navigator: OrderEditorScreenNavigator,
  extras: OrderEditorScreenExtras,
) {
  val scope = rememberCoroutineScope()
  val focusManager = LocalFocusManager.current
  val contentHeight = LocalContentHeight.current

  val onSubmit = {
    focusManager.clearFocus()

    scope.launch(Dispatchers.IO) {
      if (extras.validateChanges()) {
        val result = extras.saveChanges()
        if (result) {
          withContext(Dispatchers.Main) { navigator.navUp() }
        }
      } else {
        TextFieldValidationService.checkAll(states)
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
      state = states.customerState,
      choices = customers(),
      leadingIcon = {
        TaurusIcon(
          imageVector = Icons.Default.Person,
          contentDescription = "CustomerLeadingIcon",
          isError = states.customerState.showErrors(),
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
      state = states.titleState,
      choices = titles(),
      leadingIcon = {
        TaurusIcon(
          imageVector = Icons.Default.Title,
          contentDescription = "TitleLeadingIcon",
          isError = states.titleState.showErrors(),
        )
      },
      supportingText = { Text(text = stringResource(id = R.string.order_title_supporting_text)) },
      onImeAction = goToNext,
    )
    Spacer(modifier = Modifier.height(contentHeight.large))
    TaurusDropDown(
      state = states.modelState,
      choices = emptyList(),
      leadingIcon = {
        TaurusIcon(
          imageVector = Icons.Default.Group,
          contentDescription = "ModelLeadingIcon",
          isError = states.modelState.showErrors(),
        )
      },
      supportingText = { Text(text = stringResource(id = R.string.order_model_supporting_text)) },
      onImeAction = goToNext,
    )
    Spacer(modifier = Modifier.height(contentHeight.large))
    TaurusDropDown(
      state = states.sizeState,
      choices = sizes(),
      leadingIcon = {
        TaurusIcon(
          imageVector = Icons.Default.FormatSize,
          contentDescription = "SizeLeadingIcon",
          isError = states.sizeState.showErrors(),
        )
      },
      supportingText = { Text(text = stringResource(id = R.string.order_size_supporting_text)) },
      onImeAction = goToNext,
    )
    Spacer(modifier = Modifier.height(contentHeight.large))
    TaurusDropDown(
      state = states.colorState,
      choices = emptyList(),
      leadingIcon = {
        TaurusIcon(
          imageVector = Icons.Default.Colorize,
          contentDescription = "ColorLeadingIcon",
          isError = states.colorState.showErrors(),
        )
      },
      supportingText = { Text(text = stringResource(id = R.string.order_color_supporting_text)) },
      onImeAction = goToNext,
    )
    Spacer(modifier = Modifier.height(contentHeight.large))
    TaurusDropDown(
      state = states.colorState,
      choices = emptyList(),
      leadingIcon = {
        TaurusIcon(
          imageVector = Icons.Default.Category,
          contentDescription = "CategoryLeadingIcon",
          isError = states.categoryState.showErrors(),
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
      quantityState = states.quantityState,
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