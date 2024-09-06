package com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.composables.bars.util

data class OrderEditorScreenExtras(
  val saveChanges: suspend () -> Boolean = { false },
  val validateChanges: () -> Boolean = { false },
)