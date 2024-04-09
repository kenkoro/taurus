package com.kenkoro.taurus.client.feature.sewing.presentation.screen.login.components

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation

data class FieldData(
  val value: String,
  val hint: String = "",
  val onValueChange: (String) -> Unit,
  val placeholderText: String = "",
  val keyboardOptions: KeyboardOptions =
    KeyboardOptions.Default.copy(
      imeAction = ImeAction.Next,
      keyboardType = KeyboardType.Text,
    ),
  val keyboardActions: KeyboardActions = KeyboardActions.Default,
  val transformation: VisualTransformation = VisualTransformation.None,
)