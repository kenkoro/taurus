package com.kenkoro.taurus.client.feature.search.order.details.presentation.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.kenkoro.taurus.client.R
import com.kenkoro.taurus.client.core.local.LocalContentHeight
import com.kenkoro.taurus.client.core.local.LocalContentWidth
import com.kenkoro.taurus.client.core.local.LocalShape
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.util.CustomerState
import com.kenkoro.taurus.client.feature.shared.components.TaurusIcon
import com.kenkoro.taurus.client.feature.shared.states.TaurusTextFieldState
import com.kenkoro.taurus.client.ui.theme.AppTheme

@Composable
fun OrderDetailsSearchContent(
  modifier: Modifier = Modifier,
  state: TaurusTextFieldState,
) {
  val shape = LocalShape.current
  val contentWidth = LocalContentWidth.current
  val contentHeight = LocalContentHeight.current
  val focusManager = LocalFocusManager.current

  val interactionSource = remember { MutableInteractionSource() }

  Column(
    modifier =
      modifier
        .fillMaxWidth()
        .clickable(interactionSource = interactionSource, indication = null) {
          focusManager.clearFocus()
        },
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Column(
      modifier = Modifier.width(contentWidth.standard),
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      Spacer(modifier = Modifier.height(contentHeight.medium))
      OutlinedTextField(
        modifier =
          Modifier
            .height(contentHeight.searchBar),
        value = state.text,
        onValueChange = { /* Implement this later */ },
        isError = state.showErrors(),
        leadingIcon = {
          TaurusIcon(
            imageVector = Icons.Default.Search,
            contentDescription = "order details search content: text field leading icon",
            isError = state.showErrors(),
          )
        },
        placeholder = { Text(text = stringResource(id = R.string.order_details_search_bar)) },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
        shape = RoundedCornerShape(shape.medium),
        singleLine = true,
      )
      Spacer(modifier = Modifier.height(contentHeight.medium))
      LazyColumn {
        repeat(5) {
          item {
            Text(text = "Hello $it")
          }
        }
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
private fun OrderDetailsSearchContentPrev() {
  AppTheme {
    val state: TaurusTextFieldState = CustomerState()

    OrderDetailsSearchContent(state = state)
  }
}