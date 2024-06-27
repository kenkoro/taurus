package com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.composables

import android.os.Build
import android.os.VibrationEffect
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.kenkoro.taurus.client.core.local.LocalContentWidth
import com.kenkoro.taurus.client.core.local.LocalShape
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.util.OrderIdState
import com.kenkoro.taurus.client.feature.shared.components.TaurusIcon
import com.kenkoro.taurus.client.feature.shared.states.TaurusTextFieldState

@Composable
fun OrderId(
  modifier: Modifier = Modifier,
  orderIdState: TaurusTextFieldState = remember { OrderIdState() },
  imeAction: ImeAction = ImeAction.Next,
  onImeAction: () -> Unit = {},
) {
  val contentWidth = LocalContentWidth.current
  val shape = LocalShape.current
  val view = LocalView.current
  val vibrationEffect =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
      VibrationEffect.EFFECT_CLICK
    } else {
      null
    }
  val onClearOrderId = {
    orderIdState.text = ""

    vibrationEffect?.let {
      view.performHapticFeedback(it)
    }
    Unit
  }

  val orderIdErrorMessage = stringResource(id = R.string.order_id_error)
  val emptyTextFieldErrorMessage = stringResource(id = R.string.empty_text_field_error)
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
      Row {
        if (orderIdState.text.isNotEmpty()) {
          IconButton(onClick = onClearOrderId) {
            Icon(
              imageVector = Icons.Default.Close,
              contentDescription = "ClearOrderIdIcon",
            )
          }
          Spacer(modifier = Modifier.width(contentWidth.small))
        }
      }
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
    keyboardActions =
      KeyboardActions(
        onAny = { onImeAction() },
      ),
    supportingText = {
      // TODO: Track if the order is unique
      val errorMessage = orderIdState.getError()
      if (errorMessage == null) {
        if (!orderIdState.isFocusedOnce) {
          Text(text = stringResource(id = R.string.order_id_supporting_text))
        } else {
          if (orderIdState.text.length == 9) {
            Text(text = stringResource(id = R.string.full_order_id_supporting_text))
          }
        }
      } else {
        if (orderIdState.isBlank()) {
          Text(text = emptyTextFieldErrorMessage)
        } else {
          Text(text = errorMessage)
        }
      }
    },
    shape = RoundedCornerShape(shape.medium),
    singleLine = true,
  )
}