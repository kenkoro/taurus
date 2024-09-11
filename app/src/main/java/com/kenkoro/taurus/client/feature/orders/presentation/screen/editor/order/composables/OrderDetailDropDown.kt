package com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.composables

import android.os.Build
import android.os.VibrationEffect
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.kenkoro.taurus.client.R
import com.kenkoro.taurus.client.core.local.LocalContentHeight
import com.kenkoro.taurus.client.core.local.LocalContentWidth
import com.kenkoro.taurus.client.core.local.LocalShape
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.util.CustomerState
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.util.OrderEditorScreenNavigator
import com.kenkoro.taurus.client.feature.shared.states.TaurusTextFieldState
import com.kenkoro.taurus.client.ui.theme.AppTheme

@Composable
fun OrderDetailDropDown(
  modifier: Modifier = Modifier,
  navigator: OrderEditorScreenNavigator,
  state: TaurusTextFieldState,
  dropDownTitle: String,
  onStateChangeOrderDetailsSearchBehavior: (TaurusTextFieldState) -> Unit = {},
  placeholder: @Composable () -> Unit = {
    Text(
      text = stringResource(id = R.string.taurus_drop_down_unselected),
      color = MaterialTheme.colorScheme.onPrimaryContainer,
    )
  },
  contentColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
  containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
) {
  val shape = LocalShape.current
  val contentWidth = LocalContentWidth.current
  val contentHeight = LocalContentHeight.current
  val view = LocalView.current
  val vibrationEffect =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
      VibrationEffect.EFFECT_CLICK
    } else {
      null
    }

  val onClick = {
    vibrationEffect?.let {
      view.performHapticFeedback(it)
    }

    onStateChangeOrderDetailsSearchBehavior(state)
    navigator.toOrderDetailsSearchScreen(state)
  }

  val selectedItem = @Composable { Text(text = state.text) }

  Column(modifier = modifier) {
    Text(text = dropDownTitle, color = MaterialTheme.colorScheme.onBackground)
    Spacer(modifier = Modifier.height(contentHeight.small))
    Box(
      modifier =
        Modifier
          .size(width = contentWidth.dropDown, height = contentHeight.dropDown)
          .clip(RoundedCornerShape(shape.medium))
          .background(containerColor)
          .clickable { onClick() },
    ) {
      Row(
        modifier =
          Modifier
            .fillMaxHeight()
            .fillMaxWidth(.5F),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Spacer(modifier = Modifier.width(contentWidth.large))
        if (state.isBlank()) {
          placeholder()
        } else {
          selectedItem()
        }
      }
      Row(
        modifier =
          Modifier
            .fillMaxSize(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Icon(
          imageVector = Icons.Outlined.KeyboardArrowDown,
          contentDescription = "order detail drop down: arrow down icon",
        )
        Spacer(modifier = Modifier.width(contentWidth.extraMedium))
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
private fun OrderDetailDropDownPrev() {
  AppTheme {
    val state: TaurusTextFieldState = CustomerState()

    OrderDetailDropDown(
      state = state,
      dropDownTitle = "Customer brand-name",
      placeholder = { Text(text = "Not selected") },
      navigator = OrderEditorScreenNavigator(),
    )
  }
}