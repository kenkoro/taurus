package com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.composables

import android.os.Build
import android.os.VibrationEffect
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kenkoro.taurus.client.R
import com.kenkoro.taurus.client.core.local.LocalContentHeight
import com.kenkoro.taurus.client.core.local.LocalContentWidth
import com.kenkoro.taurus.client.core.local.LocalShape
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.util.CategoryState
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.components.item.order.OrderItemKeyboardArrowIconRow
import com.kenkoro.taurus.client.feature.shared.components.TaurusIcon
import com.kenkoro.taurus.client.feature.shared.states.TaurusTextFieldState
import com.kenkoro.taurus.client.ui.theme.AppTheme
import java.util.UUID

@Composable
fun TaurusDropDown(
  modifier: Modifier = Modifier,
  state: TaurusTextFieldState,
  choices: List<String>,
  leadingIcon: @Composable (() -> Unit)? = null,
  placeholder: @Composable (() -> Unit)? = {
    Text(
      text = stringResource(id = R.string.taurus_drop_down_unselected),
      color =
        if (state.showErrors()) {
          MaterialTheme.colorScheme.error
        } else {
          Color.Unspecified
        },
    )
  },
  keyboardOptions: KeyboardOptions =
    KeyboardOptions.Default.copy(
      imeAction = ImeAction.Next,
      keyboardType = KeyboardType.Text,
    ),
  onImeAction: () -> Unit = {},
  supportingText: @Composable (() -> Unit)? = null,
  contentColor: Color = MaterialTheme.colorScheme.onPrimary,
  containerColor: Color = MaterialTheme.colorScheme.primary,
) {
  val shape = LocalShape.current
  val focusManager = LocalFocusManager.current
  val contentWidth = LocalContentWidth.current
  val contentHeight = LocalContentHeight.current
  val view = LocalView.current
  val vibrationEffect =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
      VibrationEffect.EFFECT_CLICK
    } else {
      null
    }

  var searchedText by remember {
    mutableStateOf("")
  }
  var showChoices by remember {
    mutableStateOf(false)
  }
  val focusRequester = remember { FocusRequester() }
  val filteredItems =
    choices.filter {
      it.contains(searchedText, ignoreCase = true)
    }

  val onClearChoice = {
    searchedText = ""
    state.text = ""

    vibrationEffect?.let {
      view.performHapticFeedback(it)
    }

    focusRequester.requestFocus()
  }
  val colorBasedOnState = @Composable { color: Color, errorColor: Color ->
    if (state.showErrors()) {
      errorColor
    } else {
      color
    }
  }

  Column(
    modifier =
      modifier
        .fillMaxWidth()
        .focusRequester(focusRequester)
        .heightIn(min = 0.dp, max = contentHeight.taurusDropDownMaxHeight),
  ) {
    OutlinedTextField(
      modifier =
        Modifier
          .fillMaxWidth()
          .onFocusChanged { focusState ->
            state.onFocusChange(focusState.isFocused)
            if (!state.isFocused) {
              state.enableShowErrors()
            }
            showChoices = focusState.isFocused
          },
      value = searchedText,
      onValueChange = { searchedText = it },
      isError = state.showErrors(),
      leadingIcon = leadingIcon,
      trailingIcon = {
        Row(verticalAlignment = Alignment.CenterVertically) {
          if (searchedText.isNotEmpty()) {
            IconButton(onClick = onClearChoice) {
              Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "ClearSubjectIcon",
              )
            }
            Spacer(modifier = Modifier.width(contentWidth.small))
          }
          OrderItemKeyboardArrowIconRow(
            selected = showChoices,
            containerColor = colorBasedOnState(containerColor, MaterialTheme.colorScheme.error),
            contentColor = colorBasedOnState(contentColor, MaterialTheme.colorScheme.onError),
          )
        }
      },
      placeholder = placeholder,
      keyboardOptions = keyboardOptions,
      keyboardActions = KeyboardActions(onAny = { onImeAction() }),
      supportingText =
        if (!showChoices) {
          supportingText
        } else {
          null
        },
      shape = RoundedCornerShape(shape.medium),
      singleLine = true,
      colors =
        OutlinedTextFieldDefaults.colors(
          focusedTextColor = colorBasedOnState(Color.Unspecified, MaterialTheme.colorScheme.error),
          unfocusedTextColor =
            colorBasedOnState(
              Color.Unspecified,
              MaterialTheme.colorScheme.error,
            ),
          errorTextColor = colorBasedOnState(Color.Unspecified, MaterialTheme.colorScheme.error),
        ),
    )
    if (showChoices) {
      Spacer(modifier = Modifier.height(contentHeight.small))
      LazyColumn(
        modifier =
          Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(shape.medium))
            .background(containerColor),
      ) {
        items(
          items = filteredItems,
          key = { UUID.randomUUID().toString() },
        ) {
          TaurusDropDownItemContent(
            item = it,
            onClick = {
              searchedText = it
              state.text = it
              focusManager.clearFocus()
            },
            contentColor = contentColor,
          )
        }
      }
    }
  }
}

@Composable
fun TaurusDropDownItemContent(
  modifier: Modifier = Modifier,
  item: String,
  onClick: () -> Unit = {},
  contentColor: Color = MaterialTheme.colorScheme.onPrimary,
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
    Text(text = item, color = contentColor)
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
      leadingIcon = {
        TaurusIcon(
          imageVector = Icons.Default.Category,
          contentDescription = "CategoryLeadingIcon",
          isError = state.showErrors(),
        )
      },
    )
  }
}