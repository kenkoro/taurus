package com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.composables

import android.os.Build
import android.os.VibrationEffect
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material3.MaterialTheme
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
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.util.OrderIdState
import com.kenkoro.taurus.client.feature.shared.components.TaurusIcon
import com.kenkoro.taurus.client.feature.shared.states.TaurusTextFieldState

@Composable
@Deprecated("Due to auto-inc order id")
fun OrderId(
  modifier: Modifier = Modifier,
  orderIdState: TaurusTextFieldState = remember { OrderIdState() },
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
    orderIdState.text = ""

    vibrationEffect?.let {
      view.performHapticFeedback(it)
    }
    Unit
  }

  val orderIdErrorMessage = stringResource(id = R.string.order_id_error)
  orderIdState.setErrorMessage(orderIdErrorMessage)

  OutlinedTextField(
    modifier =
      Modifier
        .fillMaxWidth()
        .onFocusChanged { focusState ->
          orderIdState.onFocusChange(focusState.isFocused)
          if (!orderIdState.isFocused) {
            orderIdState.enableShowErrors()
          }
        },
    value = orderIdState.text,
    onValueChange = {
      if (it.length <= 9) {
        orderIdState.text = it
      }
    },
    leadingIcon = {
      TaurusIcon(
        imageVector = Icons.Default.Numbers,
        contentDescription = "OrderIdLeadingIcon",
        isError = orderIdState.showErrors(),
      )
    },
    trailingIcon = {
      TaurusTextFieldTrailingIcon(
        state = orderIdState,
        onClear = onClear,
      )
    },
    placeholder = {
      Text(text = stringResource(id = R.string.order_editor_order_id))
    },
    textStyle = MaterialTheme.typography.bodyMedium,
    isError = orderIdState.showErrors(),
    keyboardOptions =
      KeyboardOptions.Default.copy(
        imeAction = imeAction,
        keyboardType = KeyboardType.Number,
      ),
    keyboardActions = keyboardActions,
    supportingText = {
      // TODO: Track if the order is unique
      val errorMessage = orderIdState.getError()
      if (errorMessage == null) {
        if (!orderIdState.isFocusedOnce) {
          Text(text = stringResource(id = R.string.order_id_supporting_text))
        } else {
          if (orderIdState.text.length == 9) {
            Text(text = stringResource(id = R.string.max_9_supporting_text))
          }
        }
      } else {
        SupportingTextOnError(state = orderIdState, errorMessage = errorMessage)
      }
    },
    shape = RoundedCornerShape(shape.medium),
    singleLine = true,
  )
}