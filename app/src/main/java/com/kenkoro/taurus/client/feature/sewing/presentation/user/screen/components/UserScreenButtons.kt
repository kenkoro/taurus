package com.kenkoro.taurus.client.feature.sewing.presentation.user.screen.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kenkoro.taurus.client.R
import com.kenkoro.taurus.client.feature.sewing.presentation.user.screen.util.ButtonItem

@Composable
fun UserScreenButtons(
  modifier: Modifier = Modifier,
  content: @Composable (item: ButtonItem) -> Unit
) {
  val horizontalButtons = listOf(
    ButtonItem(title = stringResource(id = R.string.get_user)),
    ButtonItem(title = stringResource(id = R.string.delete_user)),
    ButtonItem(title = stringResource(id = R.string.update_user)),
    ButtonItem(title = stringResource(id = R.string.create_user))
  )
  val lazyRowState = rememberLazyListState()
  Column(modifier = modifier) {
    LazyRow(state = lazyRowState) {
      items(horizontalButtons) { item ->
        content(item)
      }
    }
  }
}