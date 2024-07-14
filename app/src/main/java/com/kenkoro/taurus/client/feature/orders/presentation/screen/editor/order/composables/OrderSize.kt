package com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.composables

import android.os.Build
import android.os.VibrationEffect
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FormatSize
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
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.util.SizeState
import com.kenkoro.taurus.client.feature.shared.components.TaurusIcon
import com.kenkoro.taurus.client.feature.shared.states.TaurusTextFieldState

@Composable
fun OrderSize(
  modifier: Modifier = Modifier,
  sizeState: TaurusTextFieldState = remember { SizeState() },
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
    sizeState.text = ""

    vibrationEffect?.let {
      view.performHapticFeedback(it)
    }
    Unit
  }

  val categoryErrorMessage = stringResource(id = R.string.size_error)
  sizeState.setErrorMessage(categoryErrorMessage)

  OutlinedTextField(
    modifier =
      modifier
        .fillMaxWidth()
        .onFocusChanged { focusState ->
          sizeState.onFocusChange(focusState.isFocused)
          if (!sizeState.isFocused) {
            sizeState.enableShowErrors()
          }
        },
    value = sizeState.text,
    onValueChange = {
      if (it.length <= 9) {
        sizeState.text = it
      }
    },
    isError = sizeState.showErrors(),
    leadingIcon = {
      TaurusIcon(
        imageVector = Icons.Default.FormatSize,
        contentDescription = "SizeLeadingIcon",
        isError = sizeState.showErrors(),
      )
    },
    trailingIcon = {
      TaurusTextFieldTrailingIcon(
        state = sizeState,
        onClear = onClear,
      )
    },
    placeholder = {
      Text(text = stringResource(id = R.string.order_editor_size))
    },
    keyboardOptions =
      KeyboardOptions.Default.copy(
        imeAction = imeAction,
        keyboardType = KeyboardType.Text,
      ),
    keyboardActions = keyboardActions,
    supportingText = {
      val errorMessage = sizeState.getError()
      if (errorMessage == null) {
        if (sizeState.isFocusedOnce && sizeState.text.length == 9) {
          Text(text = stringResource(id = R.string.max_9_supporting_text))
        }

        if (!sizeState.isFocusedOnce) {
          Text(text = stringResource(id = R.string.order_size_supporting_text))
        }
      } else {
        SupportingTextOnError(state = sizeState, errorMessage = errorMessage)
      }
    },
    shape = RoundedCornerShape(shape.medium),
    singleLine = true,
  )
}