package com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kenkoro.taurus.client.core.local.LocalContentWidth
import com.kenkoro.taurus.client.feature.shared.states.TaurusTextFieldState

@Composable
fun TaurusTextFieldTrailingIcon(
  modifier: Modifier = Modifier,
  state: TaurusTextFieldState,
  onClear: () -> Unit = {},
) {
  val contentWidth = LocalContentWidth.current

  Row(modifier) {
    if (state.text.isNotEmpty()) {
      IconButton(onClick = onClear) {
        Icon(
          imageVector = Icons.Default.Close,
          contentDescription = "ClearTrailingIcon",
        )
      }
      Spacer(modifier = Modifier.width(contentWidth.small))
    }
  }
}