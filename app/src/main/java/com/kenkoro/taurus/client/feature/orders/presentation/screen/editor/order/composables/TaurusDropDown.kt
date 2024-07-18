package com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kenkoro.taurus.client.core.local.LocalContentHeight
import com.kenkoro.taurus.client.core.local.LocalShape
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.util.CategoryState
import com.kenkoro.taurus.client.feature.shared.states.TaurusTextFieldState
import com.kenkoro.taurus.client.ui.theme.AppTheme
import java.util.UUID

@Composable
fun TaurusDropDown(
  modifier: Modifier = Modifier,
  state: TaurusTextFieldState,
  choices: List<String>,
  leadingIcon: @Composable (() -> Unit)? = null,
  trailingIcon: @Composable (() -> Unit)? = null,
  placeholder: @Composable (() -> Unit)? = null,
  keyboardOptions: KeyboardOptions =
    KeyboardOptions.Default.copy(
      imeAction = ImeAction.Next,
      keyboardType = KeyboardType.Text,
    ),
  onImeAction: () -> Unit = {},
  supportingText: @Composable (() -> Unit)? = null,
) {
  val shape = LocalShape.current
  var searchedText by remember {
    mutableStateOf("")
  }
  val snapshot = remember { mutableStateListOf(choices) }

  LaunchedEffect(searchedText) {
    snapshot
      .snapshot.filter { it.contains(searchedText, ignoreCase = true) }
  }

  Column(
    modifier =
      modifier
        .fillMaxWidth()
        .heightIn(min = 0.dp, max = 256.dp),
  ) {
    OutlinedTextField(
      modifier = Modifier.fillMaxWidth(),
      value = searchedText,
      onValueChange = { searchedText = it },
      isError = state.showErrors(),
      leadingIcon = leadingIcon,
      trailingIcon = trailingIcon,
      placeholder = placeholder,
      keyboardOptions = keyboardOptions,
      keyboardActions = KeyboardActions(onAny = { onImeAction() }),
      supportingText = supportingText,
      shape = RoundedCornerShape(topStart = shape.medium, topEnd = shape.medium),
      singleLine = true,
    )
    LazyColumn(
      modifier =
        Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(bottomStart = shape.medium, bottomEnd = shape.medium))
          .background(MaterialTheme.colorScheme.primaryContainer),
    ) {
      items(
        items = snapshot,
        key = { UUID.randomUUID().toString() },
      ) {
        TaurusDropDownItemContent(
          item = it,
          onClick = {
            state.text = it
          },
        )
      }
    }
  }
}

@Composable
fun TaurusDropDownItemContent(
  modifier: Modifier = Modifier,
  item: String,
  onClick: () -> Unit = {},
) {
  val contentHeight = LocalContentHeight.current

  Box(
    modifier =
      modifier
        .fillMaxWidth()
        .height(contentHeight.taurusDropDownItemContent)
        .clickable { onClick() },
    contentAlignment = Alignment.Center,
  ) {
    Text(text = item, color = MaterialTheme.colorScheme.onPrimaryContainer)
  }
}

@Preview(showBackground = true)
@Composable
private fun TaurusDropDownPrev() {
  val items =
    listOf(
      "Helloadsflkadjf",
      "Newadflaksdj",
      "Guyafdalskdn",
      "Meadf;lka",
      "Hiafladksjfa",
      "Byeasfdlne",
    )
  val state = remember { CategoryState() }

  AppTheme {
    TaurusDropDown(
      state = state,
      choices = items,
    )
  }
}