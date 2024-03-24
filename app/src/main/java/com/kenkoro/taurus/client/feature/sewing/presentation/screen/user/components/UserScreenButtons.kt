package com.kenkoro.taurus.client.feature.sewing.presentation.screen.user.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kenkoro.taurus.client.R
import com.kenkoro.taurus.client.core.local.LocalArrangement
import com.kenkoro.taurus.client.feature.sewing.presentation.screen.user.util.ButtonItem

@Composable
fun UserScreenButtons(
  modifier: Modifier = Modifier,
  content: @Composable (item: ButtonItem) -> Unit,
) {
  val arrangement = LocalArrangement.current

  val buttons =
    listOf(
      ButtonItem(title = stringResource(id = R.string.get_user)),
      ButtonItem(title = stringResource(id = R.string.delete_user)),
      ButtonItem(title = stringResource(id = R.string.update_user)),
      ButtonItem(title = stringResource(id = R.string.create_user)),
    )
  val lazyGridState = rememberLazyGridState()
  Column(modifier = modifier) {
    LazyVerticalGrid(
      columns = GridCells.Fixed(2),
      state = lazyGridState,
      contentPadding = PaddingValues(10.dp),
      verticalArrangement = Arrangement.spacedBy(arrangement.standard),
      horizontalArrangement = Arrangement.spacedBy(arrangement.standard),
    ) {
      items(buttons) { buttonItem ->
        content(buttonItem)
      }
    }
  }
}