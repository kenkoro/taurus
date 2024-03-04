package com.kenkoro.taurus.client.feature.sewing.presentation.login.screen.components

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.MutableState
import androidx.compose.ui.text.input.VisualTransformation

data class FieldData(
  val state: MutableState<String>,
  val placeholderText: String,
  val keyboardOptions: KeyboardOptions,
  val keyboardActions: KeyboardActions = KeyboardActions.Default,
  val transformation: VisualTransformation,
)