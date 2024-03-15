package com.kenkoro.taurus.client.feature.sewing.presentation.screen.user.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kenkoro.taurus.client.R
import com.kenkoro.taurus.client.feature.sewing.presentation.screen.user.util.ButtonItem

@Composable
fun UserScreenButtons(
  modifier: Modifier = Modifier,
  content: @Composable (item: ButtonItem) -> Unit,
) {
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
      verticalArrangement = Arrangement.spacedBy(10.dp),
      horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
      items(buttons) { buttonItem ->
        content(buttonItem)
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
private fun UserScreenButtonsPrev() {
  UserScreenButtons { item ->
    Spacer(modifier = Modifier.width(10.dp))
    Button(
      enabled = true,
      modifier =
        Modifier
          .size(width = 150.dp, height = 90.dp)
          .shadow(
            elevation = 4.dp,
            shape = RoundedCornerShape(30.dp),
          ),
      onClick = { /*TODO*/ },
      shape = RoundedCornerShape(30.dp),
    ) {
      Text(text = item.title, textAlign = TextAlign.Center)
    }
  }
}