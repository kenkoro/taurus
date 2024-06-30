package com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.composables

import android.os.Build
import android.os.VibrationEffect
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
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
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.util.CustomerState
import com.kenkoro.taurus.client.feature.shared.components.TaurusIcon
import com.kenkoro.taurus.client.feature.shared.states.TaurusTextFieldState

@Composable
fun OrderCustomer(
  modifier: Modifier = Modifier,
  customerState: TaurusTextFieldState = remember { CustomerState() },
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
    customerState.text = ""

    vibrationEffect?.let {
      view.performHapticFeedback(it)
    }
    Unit
  }

  val categoryErrorMessage = stringResource(id = R.string.customer_error)
  customerState.setErrorMessage(categoryErrorMessage)

  OutlinedTextField(
    modifier =
      Modifier
        .fillMaxWidth()
        .onFocusChanged { focusState ->
          customerState.onFocusChange(focusState.isFocused)
          if (!customerState.isFocused) {
            customerState.enableShowErrors()
          }
        },
    value = customerState.text,
    onValueChange = {
      if (it.length <= 24) {
        customerState.text = it
      }
    },
    leadingIcon = {
      TaurusIcon(
        imageVector = Icons.Default.Person,
        contentDescription = "CustomerLeadingIcon",
        isError = customerState.showErrors(),
      )
    },
    trailingIcon = {
      TaurusTextFieldTrailingIcon(
        state = customerState,
        onClear = onClear,
      )
    },
    placeholder = {
      Text(text = stringResource(id = R.string.order_editor_customer))
    },
    keyboardOptions =
      KeyboardOptions.Default.copy(
        imeAction = imeAction,
        keyboardType = KeyboardType.Text,
      ),
    keyboardActions = keyboardActions,
    supportingText = {
      val errorMessage = customerState.getError()
      if (errorMessage == null) {
        if (customerState.isFocusedOnce && customerState.text.length == 24) {
          Text(text = stringResource(id = R.string.max_24_supporting_text))
        }
      } else {
        SupportingTextOnError(state = customerState, errorMessage = errorMessage)
      }
    },
    shape = RoundedCornerShape(shape.medium),
    singleLine = true,
  )
}