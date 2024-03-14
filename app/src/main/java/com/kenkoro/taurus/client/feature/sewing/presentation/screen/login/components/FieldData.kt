package com.kenkoro.taurus.client.feature.sewing.presentation.screen.login.components

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.VisualTransformation

data class FieldData(
  val value: String,
  val onValueChange: (String) -> Unit,
  val placeholderText: String,
  val keyboardOptions: KeyboardOptions,
  val keyboardActions: KeyboardActions = KeyboardActions.Default,
  val transformation: VisualTransformation,
)