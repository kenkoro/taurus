package com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.composables

import android.os.Build
import android.os.VibrationEffect
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.kenkoro.taurus.client.R
import com.kenkoro.taurus.client.core.local.LocalShape
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.composables.util.SupportingTextOnError
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.composables.util.TaurusTextFieldTrailingIcon
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.util.QuantityState
import com.kenkoro.taurus.client.feature.shared.components.TaurusIcon
import com.kenkoro.taurus.client.feature.shared.states.TaurusTextFieldState

@Composable
fun OrderQuantity(
  modifier: Modifier = Modifier,
  quantityState: TaurusTextFieldState = remember { QuantityState() },
  imeAction: ImeAction = ImeAction.Next,
  onImeAction: () -> Unit = {},
  keyboardActions: KeyboardActions = KeyboardActions(onAny = { onImeAction() }),
) {
  val shape = LocalShape.current
  val view = LocalView.current
  val vibrationEffect =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
      VibrationEffect.EFFECT_CLICK
    } else {
      null
    }
  val onClear = {
    quantityState.text = ""

    vibrationEffect?.let {
      view.performHapticFeedback(it)
    }
    Unit
  }

  val categoryErrorMessage = stringResource(id = R.string.quantity_error)
  quantityState.setErrorMessage(categoryErrorMessage)

  OutlinedTextField(
    modifier =
      Modifier
        .fillMaxWidth()
        .onFocusChanged { focusState ->
          quantityState.onFocusChange(focusState.isFocused)
          if (!quantityState.isFocused) {
            quantityState.enableShowErrors()
          }
        },
    value = quantityState.text,
    onValueChange = {
      if (it.length <= 9) {
        quantityState.text = it
      }
    },
    leadingIcon = {
      TaurusIcon(
        imageVector = Icons.Default.AccountBalance,
        contentDescription = "QuantityLeadingIcon",
        isError = quantityState.showErrors(),
      )
    },
    trailingIcon = {
      TaurusTextFieldTrailingIcon(
        state = quantityState,
        onClear = onClear,
      )
    },
    suffix = {
      if (quantityState.text.isNotEmpty()) {
        Text(text = stringResource(id = R.string.quantity_suffix))
      }
    },
    placeholder = {
      Text(text = stringResource(id = R.string.order_editor_quantity))
    },
    keyboardOptions =
      KeyboardOptions.Default.copy(
        imeAction = imeAction,
        keyboardType = KeyboardType.Number,
      ),
    keyboardActions = keyboardActions,
    supportingText = {
      val errorMessage = quantityState.getError()
      if (errorMessage == null) {
        if (quantityState.isFocusedOnce && quantityState.text.length == 9) {
          Text(text = stringResource(id = R.string.max_9_supporting_text))
        }
      } else {
        SupportingTextOnError(state = quantityState, errorMessage = errorMessage)
      }
    },
    shape = RoundedCornerShape(shape.medium),
    singleLine = true,
  )
}