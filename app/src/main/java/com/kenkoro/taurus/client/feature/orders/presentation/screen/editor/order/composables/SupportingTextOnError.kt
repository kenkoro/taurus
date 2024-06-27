package com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.composables

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.kenkoro.taurus.client.R
import com.kenkoro.taurus.client.feature.shared.states.TaurusTextFieldState

@Composable
fun SupportingTextOnError(
  modifier: Modifier = Modifier,
  state: TaurusTextFieldState,
  errorMessage: String,
) {
  if (state.isBlank()) {
    Text(text = stringResource(id = R.string.empty_text_field_error))
  } else {
    Text(text = errorMessage)
  }
}