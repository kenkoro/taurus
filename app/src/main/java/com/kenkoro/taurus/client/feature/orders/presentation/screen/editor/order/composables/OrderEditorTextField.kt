package com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.composables

import android.os.Build
import android.os.VibrationEffect
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.kenkoro.taurus.client.R
import com.kenkoro.taurus.client.core.local.LocalShape
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.composables.util.TaurusTextFieldTrailingIcon
import com.kenkoro.taurus.client.feature.shared.components.TaurusIcon
import com.kenkoro.taurus.client.feature.shared.states.TaurusTextFieldState

@Composable
fun OrderEditorTextField(
  modifier: Modifier = Modifier,
  state: TaurusTextFieldState,
  placeholder: @Composable () -> Unit,
  supportingText: @Composable () -> Unit,
  imeAction: ImeAction = ImeAction.Next,
  onImeAction: () -> Unit = {},
  keyboardActions: KeyboardActions = KeyboardActions(onAny = { onImeAction() }),
  errorMessage: String = "",
  imageVector: ImageVector,
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
    state.text = ""

    vibrationEffect?.let {
      view.performHapticFeedback(it)
    }
    Unit
  }

  val categoryErrorMessage = stringResource(id = R.string.customer_error)
  state.setErrorMessage(categoryErrorMessage)

  OutlinedTextField(
    modifier =
      Modifier
        .fillMaxWidth()
        .onFocusChanged { focusState ->
          state.onFocusChange(focusState.isFocused)
          if (!state.isFocused) {
            state.enableShowErrors()
          }
        },
    value = state.text,
    onValueChange = {
      if (it.length <= 24) {
        state.text = it
      }
    },
    leadingIcon = {
      TaurusIcon(
        imageVector = imageVector,
        contentDescription = "LeadingIcon",
        isError = state.showErrors(),
      )
    },
    trailingIcon = {
      TaurusTextFieldTrailingIcon(
        state = state,
        onClear = onClear,
      )
    },
    placeholder = placeholder,
    keyboardOptions =
      KeyboardOptions.Default.copy(
        imeAction = imeAction,
        keyboardType = KeyboardType.Text,
      ),
    keyboardActions = keyboardActions,
    supportingText = supportingText,
    shape = RoundedCornerShape(shape.medium),
    singleLine = true,
  )
}