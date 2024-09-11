package com.kenkoro.taurus.client.feature.search.order.details.presentation.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.kenkoro.taurus.client.R
import com.kenkoro.taurus.client.core.local.LocalContentHeight
import com.kenkoro.taurus.client.core.local.LocalContentWidth
import com.kenkoro.taurus.client.core.local.LocalShape
import com.kenkoro.taurus.client.core.local.LocalSize
import com.kenkoro.taurus.client.core.local.LocalStrokeWidth
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.util.CustomerState
import com.kenkoro.taurus.client.feature.shared.components.TaurusIcon
import com.kenkoro.taurus.client.feature.shared.states.TaurusTextFieldState
import com.kenkoro.taurus.client.ui.theme.AppTheme
import java.util.UUID

@Composable
fun OrderDetailsSearchContent(
  modifier: Modifier = Modifier,
  state: TaurusTextFieldState,
  onFetchData: suspend () -> List<String> = { emptyList() },
  onNavUp: () -> Unit = {},
) {
  val shape = LocalShape.current
  val contentWidth = LocalContentWidth.current
  val contentHeight = LocalContentHeight.current
  val focusManager = LocalFocusManager.current
  val size = LocalSize.current
  val strokeWidth = LocalStrokeWidth.current

  val interactionSource = remember { MutableInteractionSource() }
  val results = remember { mutableStateListOf<String>() }
  var isDataLoading by remember {
    mutableStateOf(false)
  }
  var searchState by remember {
    mutableStateOf("")
  }

  LaunchedEffect(searchState) {
    isDataLoading = true
    if (results.isNotEmpty()) {
      results.clear()
    }
    results.addAll(onFetchData())
    isDataLoading = false
  }

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
            .fillMaxWidth()
            .height(contentHeight.searchBar),
        value = searchState,
        onValueChange = {
          if (it.length <= 20) {
            searchState = it
          }
        },
        isError = state.showErrors(),
        leadingIcon = {
          TaurusIcon(
            imageVector = Icons.Default.Search,
            contentDescription = "order details search content: text field leading icon",
            isError = state.showErrors(),
          )
        },
        placeholder = { Text(text = stringResource(id = R.string.order_details_search_bar)) },
        supportingText = {
          if (state.text.length == 20) {
            Text(text = stringResource(id = R.string.max_20_supporting_text))
          }
        },
        keyboardOptions =
          KeyboardOptions.Default.copy(
            imeAction = ImeAction.Search,
            keyboardType = KeyboardType.Text,
          ),
        keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
        shape = RoundedCornerShape(shape.medium),
        singleLine = true,
      )
      Spacer(modifier = Modifier.height(contentHeight.large))
      if (isDataLoading) {
        Box(
          modifier = Modifier.fillMaxSize(),
          contentAlignment = Alignment.Center,
        ) {
          CircularProgressIndicator(
            modifier = Modifier.size(size.medium),
            strokeWidth = strokeWidth.small,
          )
        }
      } else {
        LazyColumn(
          modifier =
            Modifier
              .weight(1F)
              .fillMaxWidth(),
          horizontalAlignment = Alignment.Start,
        ) {
          items(
            items = results,
            key = { UUID.randomUUID().toString() },
          ) { fetchedResult ->
            OrderDetailsSearchItem(
              modifier =
                Modifier.clickable {
                  searchState = fetchedResult
                  state.text = fetchedResult
                  onNavUp()
                },
              text = fetchedResult,
            )
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
    OrderDetailsSearchContent(state = state, onFetchData = { listOf("Result 1", "Result 2") })
  }
}