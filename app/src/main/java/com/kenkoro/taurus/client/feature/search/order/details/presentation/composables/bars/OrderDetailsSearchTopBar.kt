package com.kenkoro.taurus.client.feature.search.order.details.presentation.composables.bars

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.kenkoro.taurus.client.R
import com.kenkoro.taurus.client.core.local.LocalContentHeight
import com.kenkoro.taurus.client.core.local.LocalContentWidth
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.util.CategoryState
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.util.ColorState
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.util.CustomerState
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.util.ModelState
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.util.SizeState
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.util.TitleState
import com.kenkoro.taurus.client.feature.search.order.details.presentation.util.OrderDetailsSearchScreenNavigator
import com.kenkoro.taurus.client.feature.shared.states.TaurusTextFieldState
import com.kenkoro.taurus.client.ui.theme.AppTheme

@Composable
fun OrderDetailsTopBar(
  modifier: Modifier = Modifier,
  navigator: OrderDetailsSearchScreenNavigator,
  state: TaurusTextFieldState? = null,
) {
  val contentWidth = LocalContentWidth.current
  val contentHeight = LocalContentHeight.current

  Row(
    modifier =
      modifier
        .fillMaxWidth()
        .height(contentHeight.topBar)
        .background(MaterialTheme.colorScheme.background),
    horizontalArrangement = Arrangement.Start,
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Box(
      modifier =
        Modifier
          .size(contentHeight.topBar)
          .clickable { navigator.navUp() },
      contentAlignment = Alignment.Center,
    ) {
      Icon(
        imageVector = Icons.AutoMirrored.Default.KeyboardArrowLeft,
        contentDescription = "GoBackToOrderScreen",
      )
    }
    Row(
      modifier =
        Modifier
          .weight(1F)
          .fillMaxHeight(),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Spacer(modifier = Modifier.width(contentWidth.medium))
      Text(
        text = autoTitle(state),
      )
    }
  }
}

@SuppressLint("ComposableNaming")
@Composable
private fun autoTitle(state: TaurusTextFieldState?): String {
  return when (state) {
    is CustomerState -> stringResource(id = R.string.order_details_customer)
    is TitleState -> stringResource(id = R.string.order_details_title)
    is ModelState -> stringResource(id = R.string.order_details_model)
    is SizeState -> stringResource(id = R.string.order_details_size)
    is ColorState -> stringResource(id = R.string.order_details_color)
    is CategoryState -> stringResource(id = R.string.order_details_category)
    else -> ""
  }
}

@PreviewLightDark
@Composable
private fun OrderDetailsTopBarPrev() {
  AppTheme {
    OrderDetailsTopBar(navigator = OrderDetailsSearchScreenNavigator {})
  }
}